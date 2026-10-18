package com.routineflow.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.routineflow.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (
            android.os.Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        createChannel(context)
        val habitId = intent.getLongExtra(EXTRA_HABIT_ID, 0L)
        val name = intent.getStringExtra(EXTRA_HABIT_NAME) ?: "your habit"
        val emoji = intent.getStringExtra(EXTRA_HABIT_EMOJI) ?: "✨"
        val quote = QUOTES[(habitId % QUOTES.size).toInt()]
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("$emoji $name")
            .setContentText(quote)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)
    }

    companion object {
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_HABIT_NAME = "habit_name"
        const val EXTRA_HABIT_EMOJI = "habit_emoji"
        const val CHANNEL_ID = "habit_reminders"
        private val QUOTES = listOf(
            "A small step today makes tomorrow easier.",
            "You do not need perfect. You only need to begin.",
            "Your future self will thank you for showing up.",
            "Keep the promise small, kind, and doable.",
            "Momentum is built one gentle action at a time."
        )

        fun createChannel(context: Context) {
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Habit reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Gentle reminders and motivational quotes for your habits"
                }
                context.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }
        }
    }
}
