package com.example.android.movieexamprep.model;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

/**
 * MovieSearchResult which contains a LifeData<PagedList<Movie>>,
 * or a LiveData<String> of network state
 */
public class MovieSearchResult {
    public LiveData<PagedList<Movie>> data;
    public LiveData<String> networkErrors;

    public MovieSearchResult(LiveData<PagedList<Movie>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }
}
