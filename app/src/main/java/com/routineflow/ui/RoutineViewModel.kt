package com.routineflow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.routineflow.widgets.RoutineFlowWidget
import com.routineflow.widgets.RoutineFlowWeekWidget
import com.routineflow.notifications.ReminderScheduler
import com.routineflow.data.local.DailyCompletionCount
import com.routineflow.data.local.HabitEntity
import com.routineflow.data.local.TaskEntity
import com.routineflow.domain.RoutineRepository
import com.routineflow.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TodayUiState(
    val habits: List<HabitEntity> = emptyList(),
    val completedIds: Set<Long> = emptySet(),
    val recoveryCount: Int = 0,
    val activity: Map<LocalDate, Int> = emptyMap(),
    val taskActivity: Map<LocalDate, Int> = emptyMap(),
    val bonusXp: Int = 0,
    val equippedTree: String = "Auto"
) {
    val completedCount: Int get() = completedIds.size
    val progress: Float get() = if (habits.isEmpty()) 0f else completedCount.toFloat() / habits.size
    val xp: Int get() = (completedCount * 10) + bonusXp
    val currentStreak: Int
        get() {
            var day = LocalDate.now()
            var streak = 0
            
            // If today is 0, we don't immediately break if yesterday had activity (we just haven't done it yet today)
            if ((activity[day] ?: 0) == 0 && (activity[day.minusDays(1)] ?: 0) > 0) {
                day = day.minusDays(1)
            }
            
            var shieldsUsedThisMonth = 0
            val currentMonth = LocalDate.now().month
            
            while (true) {
                if ((activity[day] ?: 0) > 0) {
                    streak++
                    day = day.minusDays(1)
                } else {
                    // Try to use a Streak Freeze Shield for this missed day
                    if (shieldsUsedThisMonth < 1 && day.month == currentMonth) {
                        shieldsUsedThisMonth++
                        day = day.minusDays(1)
                    } else {
                        break
                    }
                }
            }
            return streak
        }
}

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository,
    private val taskRepository: TaskRepository,
    private val application: Application
) : ViewModel() {
    private val _bonusXp = MutableStateFlow(0)
    private val _equippedTree = MutableStateFlow("Auto")

    fun addTestXp(amount: Int = 100) {
        _bonusXp.value += amount
    }

    fun setEquippedTree(treeName: String) {
        _equippedTree.value = treeName
    }

    val tasks: StateFlow<List<TaskEntity>> = taskRepository.tasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val state: StateFlow<TodayUiState> = combine(
        combine(
            repository.habits(),
            repository.todayCompletions(),
            repository.recoveryPlans()
        ) { h, c, r -> Triple(h, c, r) },
        combine(
            repository.activityCounts(LocalDate.now().minusDays(111), LocalDate.now()),
            taskRepository.dailyCompletionCounts(LocalDate.now().minusDays(111), LocalDate.now()),
            _bonusXp,
            _equippedTree
        ) { a, ta, bxp, eq -> Flow2Data(a, ta, bxp, eq) }
    ) { part1, part2 ->
        TodayUiState(
            habits = part1.first,
            completedIds = part1.second.map { it.habitId }.toSet(),
            recoveryCount = part1.third.size,
            activity = part2.activity.associate { LocalDate.parse(it.date) to it.total },
            taskActivity = part2.taskActivity,
            bonusXp = part2.bonusXp,
            equippedTree = part2.equippedTree
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

private data class Flow2Data(
    val activity: List<DailyCompletionCount>,
    val taskActivity: Map<LocalDate, Int>,
    val bonusXp: Int,
    val equippedTree: String
)

    fun addHabit(name: String, category: String, emoji: String, reminderMinutes: Int?) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val habitId = repository.addHabit(
                HabitEntity(
                    name = name.trim(),
                    category = category,
                    emoji = emoji,
                    reminderMinutes = reminderMinutes
                )
            )
            if (reminderMinutes != null) {
                ReminderScheduler.schedule(application, habitId, name.trim(), emoji, reminderMinutes)
            }
            RoutineFlowWidget.updateAll(application)
            RoutineFlowWeekWidget.updateAll(application)
        }
    }

    fun complete(habitId: Long) {
        viewModelScope.launch {
            repository.complete(habitId)
            RoutineFlowWidget.updateAll(application)
            RoutineFlowWeekWidget.updateAll(application)
        }
    }

    fun archive(habitId: Long) {
        viewModelScope.launch {
            ReminderScheduler.cancel(application, habitId)
            repository.archiveHabit(habitId)
            RoutineFlowWidget.updateAll(application)
            RoutineFlowWeekWidget.updateAll(application)
        }
    }

    fun addTask(
        title: String,
        description: String = "",
        category: String = "General",
        priority: Int = 1,
        dueAt: Long? = null,
        estimatedMinutes: Int? = null
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskRepository.addTask(
                TaskEntity(
                    title = title.trim(),
                    description = description.trim(),
                    category = category.trim().ifBlank { "General" },
                    priority = priority.coerceIn(0, 3),
                    dueAt = dueAt,
                    estimatedMinutes = estimatedMinutes
                )
            )
        }
    }

    fun setTaskCompleted(taskId: Long, completed: Boolean) {
        viewModelScope.launch { taskRepository.setCompleted(taskId, completed) }
    }

    fun archiveTask(taskId: Long) {
        viewModelScope.launch { taskRepository.archive(taskId) }
    }
}
