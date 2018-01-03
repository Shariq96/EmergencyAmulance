package com.example.user.emergencyamulance;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;



public class myApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static boolean isActive;
    @Override
    public void onCreate()
    {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }
    public static boolean isActivityVisible(){
        return isActive;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        isActive = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isActive =true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isActive = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
