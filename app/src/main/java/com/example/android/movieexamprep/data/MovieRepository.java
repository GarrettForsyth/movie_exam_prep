package com.example.android.movieexamprep.data;

import android.util.Log;

import com.example.android.movieexamprep.api.MovieService;
import com.example.android.movieexamprep.db.MovieLocalCache;
import com.example.android.movieexamprep.model.Movie;
import com.example.android.movieexamprep.model.MovieSearchResult;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * Repository class that works with data and remote data sources.
 */
public class MovieRepository {

    private MovieService service;
    private MovieLocalCache cache;
    private static final int DATABASE_PAGE_SIZE = 20;
    private int lastRequestedPage = 1;

    @Inject
    public MovieRepository(MovieService movieService, MovieLocalCache cache) {
        this.service = movieService;
        this.cache = cache;
    }

    /**
     * Search repositories for names who match the query.
     */
    public MovieSearchResult search(String query, String language) {
        Log.d("MovieRepository", "New query: " + query);

        // Get data source factory from local cache
        DataSource.Factory<Integer, Movie> dataSourceFactory =
                cache.moviesByStringQueryOrderLanguage(query, language);

        // Construct the boundary callback
        MovieBoundaryCallback boundaryCallback = new MovieBoundaryCallback(
                query,
                service,
                cache
        );
        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

        LiveData<PagedList<Movie>> data = new LivePagedListBuilder<>(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build();

        return new MovieSearchResult(data, networkErrors);
    }
}
