package com.example.android.movieexamprep.util;

import android.util.Log;

import com.example.android.movieexamprep.model.Movie;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * FakeMovieRepository using in memory movie data from FakeMoveDataSource
 */
public class FakeMovieRepository {

    private static final String TAG = FakeMovieDataSource.class.getSimpleName();

    private FakeMovieDataSourceFactory factory = new FakeMovieDataSourceFactory();
    private LiveData<PagedList<Movie>> movies;

    {
        Log.d(TAG, "instance initializer: creating paged list livedata");
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();
        movies = new LivePagedListBuilder<>(factory, pagedListConfig).build();
    }

}
