package com.example.android.movieexamprep.di;

import android.app.Application;

import com.example.android.movieexamprep.MovieSearchApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class })
public interface AppComponent extends AndroidInjector<MovieSearchApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MovieSearchApp movieSearchApp);
}
