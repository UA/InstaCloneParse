package com.ua.instacloneparse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by ugur on 12.11.2017.
 */

public class StarterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
