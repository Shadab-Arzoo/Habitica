package com.routineflow.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.routineflow.R
import com.routineflow.data.local.RoutineDatabase
import java.time.LocalDate
import java.util.concurrent.Executors

class RoutineFlowWeekWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        updateAll(context)
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()

        fun updateAll(context: Context) {
            executor.execute {
                val today = LocalDate.now()
                val weekStart = today.minusDays(6)
                val checkIns = RoutineDatabase.getInstance(context)
                    .routineDao()
                    .completionCountBetween(weekStart.toString(), today.toString())
                val views = RemoteViews(context.packageName, R.layout.widget_week)
                views.setTextViewText(R.id.week_widget_title, "Your week")
                views.setTextViewText(R.id.week_widget_count, "$checkIns check-ins")
                views.setTextViewText(R.id.week_widget_subtitle, "Small steps add up")
                
                val intent = android.content.Intent(context, com.routineflow.MainActivity::class.java).apply {
                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = android.app.PendingIntent.getActivity(
                    context, 1, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
                
                AppWidgetManager.getInstance(context).updateAppWidget(
                    AppWidgetManager.getInstance(context)
                        .getAppWidgetIds(ComponentName(context, RoutineFlowWeekWidget::class.java)),
                    views
                )
            }
        }
    }
}
