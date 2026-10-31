package com.todolist.app;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(TodoWidgetPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
