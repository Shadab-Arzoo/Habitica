package com.routineflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE archived = 0 ORDER BY completed ASC, dueAt IS NULL, dueAt ASC, priority DESC, createdAt DESC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Query("SELECT * FROM tasks WHERE archived = 0 AND title = :title AND category = :category LIMIT 1")
    suspend fun findActiveTask(title: String, category: String): TaskEntity?

    @Query("UPDATE tasks SET completed = :completed, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Long, completed: Boolean, updatedAt: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: TaskCompletionEntity): Long

    @Query("DELETE FROM task_completions WHERE taskId = :taskId AND date = :date")
    suspend fun deleteCompletion(taskId: Long, date: String)

    @Query("SELECT date, COUNT(*) AS total FROM task_completions WHERE date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date")
    fun observeDailyCompletionCounts(startDate: String, endDate: String): Flow<List<DailyTaskCompletionCount>>

    @Query("SELECT * FROM subtasks WHERE taskId = :taskId ORDER BY position ASC, id ASC")
    fun observeSubtasks(taskId: Long): Flow<List<SubtaskEntity>>

    @Insert
    suspend fun insertSubtask(subtask: SubtaskEntity): Long

    @Query("UPDATE subtasks SET completed = :completed WHERE id = :subtaskId")
    suspend fun updateSubtaskCompletion(subtaskId: Long, completed: Boolean)

    @Query("UPDATE tasks SET archived = 1, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun archiveTask(taskId: Long, updatedAt: Long)

    @Query("SELECT * FROM tasks WHERE completed = 0 AND archived = 0 AND dueAt IS NOT NULL AND dueAt < :now ORDER BY dueAt ASC")
    fun observeOverdueTasks(now: Long): Flow<List<TaskEntity>>

    @Transaction
    suspend fun setTaskCompleted(taskId: Long, completed: Boolean, date: String): Boolean {
        if (completed) {
            val inserted = insertCompletion(TaskCompletionEntity(taskId = taskId, date = date))
            if (inserted == -1L) return false
        } else {
            deleteCompletion(taskId, date)
        }
        updateTaskCompletion(taskId, completed, System.currentTimeMillis())
        return true
    }
}

data class DailyTaskCompletionCount(
    val date: String,
    val total: Int
)
