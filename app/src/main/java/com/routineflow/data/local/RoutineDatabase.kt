package com.routineflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [HabitEntity::class, CompletionEntity::class, RecoveryPlanEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RoutineDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var instance: RoutineDatabase? = null

        fun getInstance(context: Context): RoutineDatabase =
            instance ?: synchronized(this) {
                instance ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    RoutineDatabase::class.java,
                    "routineflow.db"
                ).build().also { instance = it }
            }
    }
}
