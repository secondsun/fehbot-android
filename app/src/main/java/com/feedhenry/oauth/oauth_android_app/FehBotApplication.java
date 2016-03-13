package com.feedhenry.oauth.oauth_android_app;

import android.app.Application;

import com.feedhenry.oauth.oauth_android_app.inject.ApplicationModule;

import dagger.ObjectGraph;

/**
 * Created by summers on 3/13/16.
 */
public class FehBotApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new ApplicationModule(this));
        objectGraph.inject(this);
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
