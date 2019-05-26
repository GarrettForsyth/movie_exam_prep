package com.example.android.movieexamprep.ui;


import com.example.android.movieexamprep.data.MovieRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Factories for ViewModels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private MovieRepository repository;

    public ViewModelFactory(MovieRepository repo) {
        this.repository = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class.");
    }
}
