package com.example.android.movieexamprep.util;

import com.example.android.movieexamprep.model.Movie;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class FakeMovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private FakeMovieDataSource dataSource = new FakeMovieDataSource();

    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        return dataSource;
    }
}
