package com.routineflow.domain

import com.routineflow.data.local.CompletionEntity
import com.routineflow.data.local.DailyCompletionCount
import com.routineflow.data.local.HabitEntity
import com.routineflow.data.local.RecoveryPlanEntity
import com.routineflow.data.local.RoutineDao
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineRepository @Inject constructor(private val dao: RoutineDao) {
    fun habits(): Flow<List<HabitEntity>> = dao.observeHabits()
    fun todayCompletions(): Flow<List<CompletionEntity>> = dao.observeCompletions(LocalDate.now().toString())
    fun recoveryPlans(): Flow<List<RecoveryPlanEntity>> = dao.observeRecoveryPlans()
    fun activityCounts(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyCompletionCount>> =
        dao.observeCompletionCounts(startDate.toString(), endDate.toString())
    suspend fun addHabit(habit: HabitEntity): Long = dao.insertHabit(habit)
    suspend fun archiveHabit(id: Long) = dao.archiveHabit(id)

    suspend fun complete(habitId: Long, date: LocalDate = LocalDate.now()): Boolean =
        dao.insertCompletion(CompletionEntity(habitId = habitId, date = date.toString())) != -1L

    suspend fun createRecoveryPlan(habitId: Long, reducedTarget: Int) =
        dao.insertRecoveryPlan(RecoveryPlanEntity(habitId = habitId, reducedTarget = reducedTarget))

    suspend fun updateRecovery(id: Long, status: String) =
        dao.updateRecoveryStatus(id, status, if (status == "DISMISSED") System.currentTimeMillis() else null)
}
