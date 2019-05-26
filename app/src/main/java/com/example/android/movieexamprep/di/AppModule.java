package com.example.android.movieexamprep.di;

import android.app.Application;

import com.example.android.movieexamprep.db.MovieDao;
import com.example.android.movieexamprep.db.MovieDatabase;
import com.example.android.movieexamprep.db.MovieLocalCache;
import com.example.android.movieexamprep.ui.MainActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module(includes = { MovieServiceModule.class, ViewModelModule.class })
public abstract class AppModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();

    @Provides
    @Singleton
    static MovieDatabase provideDatabase(Application app) {
        return Room.databaseBuilder(
                app,
                MovieDatabase.class,
                "movies.db"
        ).build();
    }

    @Provides
    @Singleton
    static MovieLocalCache provideMovieLocalCache(MovieDao dao, Executor ioExecutor) {
        return new MovieLocalCache(dao, ioExecutor);
    }

    @Provides
    @Singleton
    static MovieDao provideMovieDao(MovieDatabase db) {
        return db.movieDao();
    }

    @Provides
    @Singleton
    static Executor provideExecutor() {
       return  Executors.newSingleThreadExecutor();
    }

    @Provides
    static String provideTestInjectionObject() {
        return "Hello Injection";
    }

}
