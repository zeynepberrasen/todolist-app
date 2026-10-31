package com.todolist.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TodoWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoRemoteViewsFactory(this.getApplicationContext());
    }

    public static class TodoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private final Context context;
        private final List<WidgetItem> items = new ArrayList<>();

        public static class WidgetItem {
            public String id;
            public String text;
            public boolean completed;

            public WidgetItem(String id, String text, boolean completed) {
                this.id = id;
                this.text = text;
                this.completed = completed;
            }
        }

        public TodoRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            loadData();
        }

        @Override
        public void onDataSetChanged() {
            loadData();
        }

        private void loadData() {
            items.clear();
            try {
                SharedPreferences prefs = context.getSharedPreferences("TodoWidgetPrefs", Context.MODE_PRIVATE);
                String jsonStr = prefs.getString("todos_json", "[]");
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String id = obj.optString("id", String.valueOf(i));
                    String text = obj.optString("text", "");
                    boolean completed = obj.optBoolean("completed", false);
                    items.add(new WidgetItem(id, text, completed));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            items.clear();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= items.size()) {
                return null;
            }

            try {
                WidgetItem item = items.get(position);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget_item);

                if (item.completed) {
                    // Completed item: green check icon, strikethrough text, "DONE" tag
                    views.setImageViewResource(R.id.widget_item_status_icon, R.drawable.ic_check_circle);

                    SpannableString spanText = new SpannableString(item.text);
                    spanText.setSpan(new StrikethroughSpan(), 0, spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    views.setTextViewText(R.id.widget_item_text, spanText);
                    views.setTextColor(R.id.widget_item_text, 0xFF94A3B8);

                    views.setTextViewText(R.id.widget_item_tag, "DONE");
                    views.setTextColor(R.id.widget_item_tag, 0xFF5EBB94);
                } else {
                    // Active item: blue circle icon, plain text, "ACTIVE" tag
                    views.setImageViewResource(R.id.widget_item_status_icon, R.drawable.ic_circle_outline);

                    views.setTextViewText(R.id.widget_item_text, item.text);
                    views.setTextColor(R.id.widget_item_text, 0xFF1E293B);

                    views.setTextViewText(R.id.widget_item_tag, "ACTIVE");
                    views.setTextColor(R.id.widget_item_tag, 0xFF1D5F8A);
                }

                // Fill-in Intent to launch app on item click
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra("todo_id", item.id);
                views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

                return views;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
