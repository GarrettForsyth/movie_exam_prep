package com.example.android.movieexamprep;

import android.app.Activity;
import android.app.Application;

import com.example.android.movieexamprep.di.DaggerAppComponent;
import com.example.android.movieexamprep.ui.MainActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/**
 * A separate app for test to avoid initializing dependency injection.
 */

public class TestApp extends Application {
}
