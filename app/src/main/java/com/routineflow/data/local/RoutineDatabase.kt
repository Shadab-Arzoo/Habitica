package com.routineflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [
        HabitEntity::class,
        CompletionEntity::class,
        RecoveryPlanEntity::class,
        TaskEntity::class,
        SubtaskEntity::class,
        TaskCompletionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RoutineDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun taskDao(): TaskDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS tasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        dueAt INTEGER,
                        priority INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        estimatedMinutes INTEGER,
                        energy INTEGER NOT NULL,
                        recurrenceRule TEXT,
                        reminderMinutes INTEGER,
                        completed INTEGER NOT NULL,
                        archived INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tasks_dueAt ON tasks(dueAt)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tasks_completed ON tasks(completed)")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_tasks_category ON tasks(category)")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS subtasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        taskId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        completed INTEGER NOT NULL,
                        position INTEGER NOT NULL,
                        FOREIGN KEY(taskId) REFERENCES tasks(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_subtasks_taskId ON subtasks(taskId)")
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS task_completions (
                        taskId INTEGER NOT NULL,
                        date TEXT NOT NULL,
                        completedAt INTEGER NOT NULL,
                        xpAwarded INTEGER NOT NULL,
                        PRIMARY KEY(taskId, date),
                        FOREIGN KEY(taskId) REFERENCES tasks(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                database.execSQL("CREATE INDEX IF NOT EXISTS index_task_completions_date ON task_completions(date)")
            }
        }

        @Volatile
        private var instance: RoutineDatabase? = null

        fun getInstance(context: Context): RoutineDatabase =
            instance ?: synchronized(this) {
                instance ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    RoutineDatabase::class.java,
                    "routineflow.db"
                ).addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}
