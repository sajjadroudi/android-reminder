package com.mobiliha.eventsbadesaba;

import android.app.Application;
import android.content.Context;

public class ReminderApp extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return applicationContext;
    }

}
