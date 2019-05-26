package com.example.android.movieexamprep.di;


import com.example.android.movieexamprep.data.MovieRepository;
import com.example.android.movieexamprep.ui.MovieViewModel;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ViewModelModule {

    @Provides
    static ViewModel provideMovieViewModel(MovieRepository repo) {
        return new MovieViewModel(repo);
    }
}
