package com.routineflow.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.routineflow.MainActivity
import com.routineflow.R

class QuickAddWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (id in ids) {
            val views = RemoteViews(context.packageName, R.layout.widget_quick_add)

            val taskIntent = Intent(context, MainActivity::class.java).apply {
                action = "com.routineflow.ACTION_ADD_TASK"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val taskPendingIntent = PendingIntent.getActivity(
                context, 101, taskIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.btn_add_task, taskPendingIntent)

            val habitIntent = Intent(context, MainActivity::class.java).apply {
                action = "com.routineflow.ACTION_ADD_HABIT"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val habitPendingIntent = PendingIntent.getActivity(
                context, 102, habitIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.btn_add_habit, habitPendingIntent)

            manager.updateAppWidget(id, views)
        }
    }
}
