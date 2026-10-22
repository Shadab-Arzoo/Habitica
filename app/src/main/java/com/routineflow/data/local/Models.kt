package com.routineflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String = "Personal",
    val emoji: String = "✨",
    val weekdays: String = "1,2,3,4,5,6,7",
    val target: Int = 1,
    val unit: String = "times",
    val reminderMinutes: Int? = null,
    val colorArgb: Long = 0xFF6C63FF,
    val archived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "completions",
    primaryKeys = ["habitId", "date"],
    foreignKeys = [androidx.room.ForeignKey(
        entity = HabitEntity::class,
        parentColumns = ["id"],
        childColumns = ["habitId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )],
    indices = [androidx.room.Index("habitId"), androidx.room.Index("date")]
)
data class CompletionEntity(
    val habitId: Long,
    val date: String,
    val quantity: Int = 1,
    val xpAwarded: Int = 10,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recovery_plans")
data class RecoveryPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val status: String = "SUGGESTED",
    val reducedTarget: Int = 10,
    val createdAt: Long = System.currentTimeMillis(),
    val dismissedAt: Long? = null
)

@Entity(
    tableName = "tasks",
    indices = [
        androidx.room.Index("dueAt"),
        androidx.room.Index("completed"),
        androidx.room.Index("category")
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueAt: Long? = null,
    val priority: Int = 1,
    val category: String = "General",
    val estimatedMinutes: Int? = null,
    val energy: Int = 1,
    val recurrenceRule: String? = null,
    val reminderMinutes: Int? = null,
    val completed: Boolean = false,
    val archived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "subtasks",
    foreignKeys = [androidx.room.ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )],
    indices = [androidx.room.Index("taskId")]
)
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long,
    val title: String,
    val completed: Boolean = false,
    val position: Int = 0
)

@Entity(
    tableName = "task_completions",
    primaryKeys = ["taskId", "date"],
    foreignKeys = [androidx.room.ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )],
    indices = [androidx.room.Index("date")]
)
data class TaskCompletionEntity(
    val taskId: Long,
    val date: String,
    val completedAt: Long = System.currentTimeMillis(),
    val xpAwarded: Int = 10
)
