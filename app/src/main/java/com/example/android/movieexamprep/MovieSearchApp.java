package com.example.android.movieexamprep;

import android.app.Activity;
import android.app.Application;

import com.example.android.movieexamprep.di.AppInjector;
import com.example.android.movieexamprep.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MovieSearchApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }
}
