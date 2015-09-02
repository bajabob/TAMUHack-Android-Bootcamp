package com.tamuhack.bootcamp;

import android.app.Application;

/**
 * Created by bob
 */
public class BootcampApplication extends Application{

    public static final String API_PARSE_APP_ID = "";
    public static final String API_PARSE_APP_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // TODO hide these keys
        // Add your initialization code here
        Parse.initialize(this, API_PARSE_APP_ID, API_PARSE_APP_KEY);

    }


}
