package com.example.android.movieexamprep.db;

import android.util.Log;

import com.example.android.movieexamprep.api.MovieServiceUtil;
import com.example.android.movieexamprep.model.Movie;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.paging.DataSource;

/**
 * Class that handles the DAO local data source. This ensures that
 * methods are triggered on the correct executor.
 */
public class MovieLocalCache {

    private MovieDao movieDao;
    private Executor ioExecutor;

    @Inject
    public MovieLocalCache(MovieDao dao, Executor ioExecutor) {
        this.movieDao = dao;
        this.ioExecutor = ioExecutor;
    }

    /**
     * Insert a list of movies in the database on a background thread.
     */
    public void insert(List<Movie> movies, MovieServiceUtil.OnInsertFinishedCallback callback) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("MovieLocalCache", String.format("Inserting %d movies.", movies.size()));
                movieDao.insert(movies);
                callback.onInsertFinished();
            }
        });
    }

    /**
     * Request a DataSource.Factory<Int, Movie> from the Dao
     * based on a string query.
     */
    public DataSource.Factory<Integer, Movie> moviesByStringQuery(String stringQuery) {
        return movieDao.moviesByQueryString('%' + stringQuery + '%');
    }

    /**
     * Request a DataSource.Factory<Int, Movie> from the Dao
     * based on a string query and language.
     */
    public DataSource.Factory<Integer, Movie> moviesByStringQueryOrderLanguage(
            String stringQuery, String language) {
        return movieDao.moviesByQueryStringFilterLanguage('%' + stringQuery + '%', '%' + language + '%');
    }

}
