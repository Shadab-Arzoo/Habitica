package com.routineflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM habits WHERE archived = 0 ORDER BY createdAt DESC")
    fun observeHabits(): Flow<List<HabitEntity>>

    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Query("UPDATE habits SET archived = 1 WHERE id = :id")
    suspend fun archiveHabit(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: CompletionEntity): Long

    @Query("SELECT * FROM completions WHERE date = :date")
    fun observeCompletions(date: String): Flow<List<CompletionEntity>>

    @Query("SELECT date, SUM(quantity) AS total FROM completions WHERE date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date")
    fun observeCompletionCounts(startDate: String, endDate: String): Flow<List<DailyCompletionCount>>

    @Query("SELECT * FROM completions WHERE habitId = :habitId ORDER BY date DESC")
    suspend fun completionsForHabit(habitId: Long): List<CompletionEntity>

    @Query("SELECT * FROM recovery_plans WHERE status = 'SUGGESTED' ORDER BY createdAt DESC")
    fun observeRecoveryPlans(): Flow<List<RecoveryPlanEntity>>

    @Insert
    suspend fun insertRecoveryPlan(plan: RecoveryPlanEntity): Long

    @Query("UPDATE recovery_plans SET status = :status, dismissedAt = :timestamp WHERE id = :id")
    suspend fun updateRecoveryStatus(id: Long, status: String, timestamp: Long? = null)

    @Query("SELECT COUNT(*) FROM completions WHERE habitId = :habitId AND date = :date")
    suspend fun completionCount(habitId: Long, date: String): Int

    @Query("SELECT * FROM completions WHERE date = :date")
    fun todayCompletionsSnapshot(date: String): List<CompletionEntity>

    @Query("SELECT COUNT(*) FROM habits WHERE archived = 0")
    fun activeHabitCount(): Int

    @Query("SELECT COUNT(*) FROM completions WHERE date BETWEEN :startDate AND :endDate")
    fun completionCountBetween(startDate: String, endDate: String): Int

    @Query("SELECT * FROM habits WHERE archived = 0 AND reminderMinutes IS NOT NULL")
    fun reminderHabits(): List<HabitEntity>

    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun observeJournal(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(entry: JournalEntity): Long

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournal(id: Long)
}

@androidx.room.Entity(tableName = "journal_entries")
data class JournalEntity(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "Reflection",
    val emoji: String = "📔",
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyCompletionCount(
    val date: String,
    val total: Int
)
