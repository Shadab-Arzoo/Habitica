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
        
        val launchIntent = Intent(context, Class.forName("com.routineflow.MainActivity")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "com.routineflow.ACTION_CHECKIN_HABIT"
            putExtra("habit_id", habitId)
        }
        val pendingIntent = android.app.PendingIntent.getActivity(
            context,
            habitId.toInt(),
            launchIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Time for $emoji $name")
            .setContentText(quote)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentTitle("RoutineFlow reminder")
                    .setContentText("You have a habit reminder.")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build()
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        try {
            NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)
        } catch (_: SecurityException) {}
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

object FocusNotificationHelper {
    private const val FOCUS_CHANNEL_ID = "focus_timer_channel"
    private const val FOCUS_NOTIFICATION_ID = 9001

    fun updateFocusNotification(context: Context, isRunning: Boolean, secondsLeft: Int, totalMinutes: Int) {
        val notificationManager = NotificationManagerCompat.from(context)

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                FOCUS_CHANNEL_ID,
                "Focus Timer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Active countdown for Pomodoro Focus sessions"
                setShowBadge(false)
            }
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            nm?.createNotificationChannel(channel)
        }

        if (!isRunning || secondsLeft <= 0) {
            notificationManager.cancel(FOCUS_NOTIFICATION_ID)
            if (secondsLeft <= 0 && isRunning) {
                val completeNotification = NotificationCompat.Builder(context, FOCUS_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("🎉 Focus Session Complete!")
                    .setContentText("You completed $totalMinutes minutes of deep focus. Great job!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()
                try {
                    notificationManager.notify(FOCUS_NOTIFICATION_ID + 1, completeNotification)
                } catch (_: SecurityException) {}
            }
            return
        }

        if (
            android.os.Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        val timeFormatted = String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = intent?.let {
            android.app.PendingIntent.getActivity(
                context, 0, it,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(context, FOCUS_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("🧘 Focus Session Active")
            .setContentText("$timeFormatted remaining • Keep flowing")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        }

        try {
            notificationManager.notify(FOCUS_NOTIFICATION_ID, builder.build())
        } catch (_: SecurityException) {}
    }
}
