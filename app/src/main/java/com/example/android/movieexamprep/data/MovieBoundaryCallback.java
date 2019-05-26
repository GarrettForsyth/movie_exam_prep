package com.example.android.movieexamprep.data;

import android.util.Log;

import com.example.android.movieexamprep.api.MovieService;
import com.example.android.movieexamprep.api.MovieServiceUtil;
import com.example.android.movieexamprep.db.MovieLocalCache;
import com.example.android.movieexamprep.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

public class MovieBoundaryCallback extends PagedList.BoundaryCallback<Movie> {

    private static final int NETWORK_PAGE_SIZE = 50;

    private String query;
    private MovieService service;
    private MovieLocalCache cache;

    // Keep the last request paged.
    // When the request is successful, increment the page number
    private int lastRequestedPage = 1;

    private boolean isRequestInProgress = false;

    private MutableLiveData<String> networkErrors;

    public MovieBoundaryCallback(String query, MovieService service, MovieLocalCache cache) {
        this.query = query;
        this.service = service;
        this.cache = cache;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    @Override
    public void onZeroItemsLoaded() {
        requestAndSaveData(query);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Movie itemAtEnd) {
        requestAndSaveData(query);
    }

    private void requestAndSaveData(String query) {
        if (isRequestInProgress) return;
        isRequestInProgress = true;

        MovieServiceUtil.searchMoviesByStringQuery(
                service,
                query,
                lastRequestedPage,
                NETWORK_PAGE_SIZE,
                new MovieServiceUtil.OnSuccessCallback() {
                    @Override
                    public void onSuccess(List<Movie> movies) {
                        for (Movie movie: movies) {
                            Log.d("MovieCount", "MovieBoundaryCallback::requestAndSAveData::onSuccess: adding " + movie.title);
                        }
                        cache.insert(
                                movies,
                                new MovieServiceUtil.OnInsertFinishedCallback() {
                                    @Override
                                    public void onInsertFinished() {
                                        lastRequestedPage++;
                                        isRequestInProgress = false;
                                    }
                                }
                        );
                    }
                },
                new MovieServiceUtil.OnErrorCallback() {
                    @Override
                    public void onError(String error) {
                        networkErrors.postValue(error);
                        isRequestInProgress = false;
                    }
                }
        );
    }

}
