package com.todolist.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import org.json.JSONArray;
import org.json.JSONObject;

public class TodoWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget_layout);

            // Bind RemoteViewsService for ListView
            Intent serviceIntent = new Intent(context, TodoWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

            // Calculate counts from SharedPreferences
            int totalCount = 0;
            int activeCount = 0;
            try {
                SharedPreferences prefs = context.getSharedPreferences("TodoWidgetPrefs", Context.MODE_PRIVATE);
                String jsonStr = prefs.getString("todos_json", "[]");
                JSONArray array = new JSONArray(jsonStr);
                totalCount = array.length();
                for (int i = 0; i < totalCount; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (!obj.optBoolean("completed", false)) {
                        activeCount++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String badgeText;
            if (totalCount == 0) {
                badgeText = "0 tasks";
            } else if (activeCount == 0) {
                badgeText = "All done";
            } else {
                badgeText = activeCount + " active · " + totalCount + " total";
            }
            views.setTextViewText(R.id.widget_count_badge, badgeText);

            // Launch app when tapping widget header or empty view
            Intent appIntent = new Intent(context, MainActivity.class);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            int immutableFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                immutableFlags |= PendingIntent.FLAG_IMMUTABLE;
            }
            PendingIntent headerPendingIntent = PendingIntent.getActivity(context, 1, appIntent, immutableFlags);
            views.setOnClickPendingIntent(R.id.widget_header, headerPendingIntent);
            views.setOnClickPendingIntent(R.id.widget_empty_view, headerPendingIntent);

            // Template PendingIntent for collection items MUST BE MUTABLE on Android 12+ (API 31+)
            int mutableFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mutableFlags |= PendingIntent.FLAG_MUTABLE;
            }
            PendingIntent templatePendingIntent = PendingIntent.getActivity(context, 0, appIntent, mutableFlags);
            views.setPendingIntentTemplate(R.id.widget_list_view, templatePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
