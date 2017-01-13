package com.threerosaty;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;

/**
 * Created by akshay on 03-01-2017.
 */
public class ThreeRosatyAppApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
