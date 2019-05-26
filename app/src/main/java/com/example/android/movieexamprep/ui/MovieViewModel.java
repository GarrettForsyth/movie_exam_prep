package com.example.android.movieexamprep.ui;

import android.util.Log;

import com.example.android.movieexamprep.data.MovieRepository;
import com.example.android.movieexamprep.model.Movie;
import com.example.android.movieexamprep.model.MovieSearchResult;

import javax.inject.Inject;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

/**
 * ViewModel for [MainActivity] screen.
 * The ViewModel works with the [MovieRepository] to get data.
 */
public class MovieViewModel extends ViewModel {


    private MovieRepository repository;

    private MutableLiveData<Pair<String, String>> queryLiveData = new MutableLiveData<>();

    private LiveData<MovieSearchResult> movieResult =
            Transformations.map(queryLiveData, it -> {
                Log.d("MovieCount", "SearchMoviesViewModel: searching Repo for: " + queryLiveData.getValue());
                MovieSearchResult result = repository.search(it.first, it.second);

                if (result != null && result.data.getValue() != null) {
                    Log.d("Transform", "movies found: " + result.data.getValue().size());
                }
                return result;
            });

    private LiveData<PagedList<Movie>> movies =
            Transformations.switchMap(movieResult, it -> {
                if (it != null) return it.data;
                else return null;
            });


    LiveData<String> networkErrors =
            Transformations.switchMap(movieResult, it -> it.networkErrors);

    @Inject
    public MovieViewModel(MovieRepository repo) {
        this.repository = repo;
    }


    /**
     * Search a movie based on a query string.
     */
    void searchMovie(String queryString, String language) {
        queryLiveData.postValue(new Pair<String, String>(queryString, language));
    }

    /**
     * Get the last value.
     */
    String lastQueryValue() {
        return queryLiveData.getValue().first;
    }

    /**
     * Get list of movies
     */
    public LiveData<PagedList<Movie>> getMovies() {
        return movies;
    }

    /**
     * Get network errors
     */
    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    /**
     * Get the repository the ViewModel is searching from.
     */
    public MovieRepository getRepository() {
        return this.repository;
    }

}
