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
