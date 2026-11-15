package com.routineflow.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.routineflow.data.local.CompletionEntity
import com.routineflow.data.local.RoutineDao
import com.routineflow.widgets.RoutineFlowWeekWidget
import com.routineflow.widgets.RoutineFlowWidget
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderReceiverEntryPoint {
    fun routineDao(): RoutineDao
}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_COMPLETE_HABIT_DIRECT -> {
                val habitId = intent.getLongExtra(EXTRA_HABIT_ID, 0L)
                val habitName = intent.getStringExtra(EXTRA_HABIT_NAME) ?: "Habit"

                // Cancel notification
                NotificationManagerCompat.from(context).cancel(habitId.toInt())

                // Insert completion into DB
                try {
                    val entryPoint = EntryPointAccessors.fromApplication(
                        context.applicationContext,
                        ReminderReceiverEntryPoint::class.java
                    )
                    val dao = entryPoint.routineDao()

                    GlobalScope.launch(Dispatchers.IO) {
                        dao.insertCompletion(
                            CompletionEntity(
                                habitId = habitId,
                                date = LocalDate.now().toString()
                            )
                        )
                        RoutineFlowWidget.updateAll(context)
                        RoutineFlowWeekWidget.updateAll(context)
                    }

                    Toast.makeText(context, "🎉 $habitName completed! +25 Drops added.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            ACTION_SNOOZE_HABIT -> {
                val habitId = intent.getLongExtra(EXTRA_HABIT_ID, 0L)
                val habitName = intent.getStringExtra(EXTRA_HABIT_NAME) ?: "Habit"
                val emoji = intent.getStringExtra(EXTRA_HABIT_EMOJI) ?: "✨"

                // Cancel current notification
                NotificationManagerCompat.from(context).cancel(habitId.toInt())

                // Schedule snooze alarm for 15 minutes later
                val alarmManager = context.getSystemService(AlarmManager::class.java)
                val snoozeTime = System.currentTimeMillis() + 15 * 60 * 1000L

                val reminderIntent = Intent(context, ReminderReceiver::class.java).apply {
                    putExtra(EXTRA_HABIT_ID, habitId)
                    putExtra(EXTRA_HABIT_NAME, habitName)
                    putExtra(EXTRA_HABIT_EMOJI, emoji)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    habitId.toInt(),
                    reminderIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                try {
                    alarmManager?.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        snoozeTime,
                        pendingIntent
                    )
                    Toast.makeText(context, "⏰ Snoozed $habitName for 15 minutes.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
                // Standard Reminder Notification Trigger
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

                // Time-adaptive quote variation
                val currentHour = LocalTime.now().hour
                val quoteList = when {
                    currentHour < 12 -> MORNING_QUOTES
                    currentHour < 18 -> AFTERNOON_QUOTES
                    else -> EVENING_QUOTES
                }
                val rawQuote = quoteList[(habitId % quoteList.size).toInt()]
                val quote = rawQuote.replace("{name}", name).replace("{emoji}", emoji)

                val launchIntent = Intent(context, Class.forName("com.routineflow.MainActivity")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = "com.routineflow.ACTION_CHECKIN_HABIT"
                    putExtra("habit_id", habitId)
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    habitId.toInt(),
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Direct Action Buttons
                val completeIntent = Intent(context, ReminderReceiver::class.java).apply {
                    action = ACTION_COMPLETE_HABIT_DIRECT
                    putExtra(EXTRA_HABIT_ID, habitId)
                    putExtra(EXTRA_HABIT_NAME, name)
                }
                val completePendingIntent = PendingIntent.getBroadcast(
                    context,
                    (habitId * 10 + 1).toInt(),
                    completeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val snoozeIntent = Intent(context, ReminderReceiver::class.java).apply {
                    action = ACTION_SNOOZE_HABIT
                    putExtra(EXTRA_HABIT_ID, habitId)
                    putExtra(EXTRA_HABIT_NAME, name)
                    putExtra(EXTRA_HABIT_EMOJI, emoji)
                }
                val snoozePendingIntent = PendingIntent.getBroadcast(
                    context,
                    (habitId * 10 + 2).toInt(),
                    snoozeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentTitle("$emoji Time for $name")
                    .setContentText(quote)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.checkbox_on_background, "✅ Complete", completePendingIntent)
                    .addAction(android.R.drawable.ic_menu_recent_history, "⏰ Snooze 15m", snoozePendingIntent)
                    .setColor(0xFF38BDF8.toInt())
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setPublicVersion(
                        NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_popup_reminder)
                            .setContentTitle("RoutineFlow Habit Reminder")
                            .setContentText("Time for your daily habit check-in.")
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
        }
    }

    companion object {
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_HABIT_NAME = "habit_name"
        const val EXTRA_HABIT_EMOJI = "habit_emoji"
        const val ACTION_COMPLETE_HABIT_DIRECT = "com.routineflow.ACTION_COMPLETE_HABIT_DIRECT"
        const val ACTION_SNOOZE_HABIT = "com.routineflow.ACTION_SNOOZE_HABIT"
        const val CHANNEL_ID = "habit_reminders"

        private val MORNING_QUOTES = listOf(
            "🌅 Start your morning with intention — time for {emoji} {name}!",
            "☀️ A gentle morning habit sets the tone for your whole day.",
            "🌱 Feed your 3D Garden this morning! {emoji} {name} is waiting.",
            "✨ Small promises kept each morning build unstoppable confidence.",
            "🌄 Morning momentum begins with one kind, doable action."
        )

        private val AFTERNOON_QUOTES = listOf(
            "⚡ Mid-day energy check: time for {emoji} {name}!",
            "🎯 Pause for 5 minutes and focus on {name}.",
            "💧 Collect +25 Water Drops for your 3D Garden tree!",
            "✨ Consistency over intensity. Show up for {name} today.",
            "🌈 You're halfway through the day — keep your rhythm alive!"
        )

        private val EVENING_QUOTES = listOf(
            "🌙 Wind down your evening with {emoji} {name}.",
            "🌌 Reflect on today's progress and check off {name}.",
            "💤 A peaceful evening routine leads to rest and renewal.",
            "✨ You showed up today! Finish strong with {name}.",
            "⭐ Keep your habit streak shining bright before bed."
        )

        fun createChannel(context: Context) {
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Habit Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Interactive habit check-in reminders & motivational quotes"
                }
                context.getSystemService(NotificationManager::class.java)
                    ?.createNotificationChannel(channel)
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
