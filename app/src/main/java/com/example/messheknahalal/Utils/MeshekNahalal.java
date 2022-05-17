package com.example.messheknahalal.Utils;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class MeshekNahalal extends Application implements AppForegroundStateManager.OnAppForegroundStateChangeListener {

    @Override
    public void onCreate() {
        super.onCreate();
        AppForegroundStateManager.getInstance().addListener(this);
    }

    @Override
    public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState newState) {
        if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND == newState) {
            // App just entered the foreground. Do something here!
        } else {
            // App just entered the background. Do something here!
            SQLiteDatabase db = openOrCreateDatabase(Utils.DATABASE_NAME, MODE_PRIVATE, null);

            Utils.deleteTable(db);
        }
    }

}
