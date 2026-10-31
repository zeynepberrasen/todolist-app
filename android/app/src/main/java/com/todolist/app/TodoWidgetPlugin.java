package com.todolist.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TodoWidget")
public class TodoWidgetPlugin extends Plugin {

    @PluginMethod
    public void syncTodos(PluginCall call) {
        try {
            JSArray todos = call.getArray("todos");
            String jsonString = todos != null ? todos.toString() : "[]";

            // Persist to SharedPreferences for Android Widget access
            Context context = getContext();
            SharedPreferences prefs = context.getSharedPreferences("TodoWidgetPrefs", Context.MODE_PRIVATE);
            prefs.edit().putString("todos_json", jsonString).apply();

            // Notify and update all active Android home screen widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, TodoWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            if (appWidgetIds != null && appWidgetIds.length > 0) {
                for (int id : appWidgetIds) {
                    TodoWidgetProvider.updateAppWidget(context, appWidgetManager, id);
                }
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            }

            call.resolve();
        } catch (Exception e) {
            e.printStackTrace();
            call.reject("Failed to sync todos with Android Widget: " + e.getMessage());
        }
    }
}
