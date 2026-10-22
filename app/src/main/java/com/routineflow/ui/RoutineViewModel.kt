package com.routineflow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.routineflow.widgets.RoutineFlowWidget
import com.routineflow.widgets.RoutineFlowWeekWidget
import com.routineflow.notifications.ReminderScheduler
import com.routineflow.data.local.HabitEntity
import com.routineflow.data.local.TaskEntity
import com.routineflow.domain.RoutineRepository
import com.routineflow.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
    val activity: Map<LocalDate, Int> = emptyMap()
) {
    val completedCount: Int get() = completedIds.size
    val progress: Float get() = if (habits.isEmpty()) 0f else completedCount.toFloat() / habits.size
    val xp: Int get() = completedCount * 10
    val currentStreak: Int
        get() {
            var day = LocalDate.now()
            var streak = 0
            while ((activity[day] ?: 0) > 0) {
                streak++
                day = day.minusDays(1)
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
    val tasks: StateFlow<List<TaskEntity>> = taskRepository.tasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val state: StateFlow<TodayUiState> = combine(
        repository.habits(),
        repository.todayCompletions(),
        repository.recoveryPlans(),
        repository.activityCounts(LocalDate.now().minusDays(111), LocalDate.now())
    ) { habits, completions, recovery, activity ->
        TodayUiState(
            habits = habits,
            completedIds = completions.map { it.habitId }.toSet(),
            recoveryCount = recovery.size,
            activity = activity.associate { LocalDate.parse(it.date) to it.total }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

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

    fun completeTask(taskId: Long) {
        viewModelScope.launch { taskRepository.complete(taskId) }
    }

    fun archiveTask(taskId: Long) {
        viewModelScope.launch { taskRepository.archive(taskId) }
    }
}
