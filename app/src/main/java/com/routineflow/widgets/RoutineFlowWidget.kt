package com.routineflow.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.routineflow.R
import com.routineflow.data.local.RoutineDatabase
import java.time.LocalDate
import java.util.concurrent.Executors

class RoutineFlowWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        updateAll(context)
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()

        fun updateAll(context: Context) {
            executor.execute {
                try {
                    val db = RoutineDatabase.getInstance(context)
                    val date = LocalDate.now().toString()
                    val completions = db.routineDao().todayCompletionsSnapshot(date)
                    val habits = db.routineDao().activeHabitCount()
                    val views = RemoteViews(context.packageName, R.layout.widget_today)
                    views.setTextViewText(R.id.widget_title, "RoutineFlow Today")
                    views.setTextViewText(R.id.widget_progress, "${completions.size} of $habits completed")
                    views.setProgressBar(R.id.widget_progress_bar, habits.coerceAtLeast(1) * 100, completions.size * 100, false)
                    views.setTextViewText(R.id.widget_streak, "✨ Keep your rhythm going")
                    
                    val intent = android.content.Intent(context, com.routineflow.MainActivity::class.java).apply {
                        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent = android.app.PendingIntent.getActivity(
                        context, 0, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
                    
                    manager(context).updateAppWidget(ids(context), views)
                } catch (error: Exception) {
                    Log.e("RoutineFlowWidget", "Unable to refresh today widget", error)
                }
            }
        }

        private fun manager(context: Context): AppWidgetManager = AppWidgetManager.getInstance(context)
        private fun ids(context: Context): IntArray =
            manager(context).getAppWidgetIds(ComponentName(context, RoutineFlowWidget::class.java))
    }
}
