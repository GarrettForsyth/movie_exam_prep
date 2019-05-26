package com.example.android.movieexamprep.util;

import android.app.Application;
import android.content.Context;

import com.example.android.movieexamprep.TestApp;

import androidx.test.runner.AndroidJUnitRunner;

/**
 * Custom runner to disable dependency injection.
 */
public class MovieTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, TestApp.class.getName(), context);
    }
}
