package com.routineflow.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.routineflow.data.local.RoutineDatabase
import java.util.concurrent.Executors

class ReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        Executors.newSingleThreadExecutor().execute {
            try {
                RoutineDatabase.getInstance(context).routineDao().reminderHabits().forEach { habit ->
                    habit.reminderMinutes?.let { minutes ->
                        ReminderScheduler.schedule(context, habit.id, habit.name, habit.emoji, minutes)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
