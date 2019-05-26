package com.example.android.movieexamprep.api;

import android.util.Log;

import com.example.android.movieexamprep.model.Movie;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieServiceUtil {

    private static final String TAG = MovieService.class.getSimpleName();
    private static final String IN_QUALIFIER = "in:title,overview";

    static final String BASE_URL = "https://api.themoviedb.org";
    public static final String POSTER_PATH_BASE = "https://image.tmdb.org/t/p/w45";

    public static void searchMoviesByStringQuery(
            MovieService service,
            String query,
            int page,
            int itemsPerPage,
            final OnSuccessCallback successCallback,
            final OnErrorCallback errorCallback
    ){
        Log.d(TAG, String.format("Query, %s, page: %d, itemsPerPage: %d",
                query, page, itemsPerPage));

        String apiQuery = query;
        
        service.searchMoviesByStringQuery(apiQuery, page, itemsPerPage).enqueue(
                new Callback<MovieSearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                        Log.d(TAG, "onResponse: got a response " + response);
                        if (response.isSuccessful()) {
                            //Log.d(TAG, "onResponse: response successful.");
                            List<Movie> movies;
                            if (response.body() != null) {
                                Log.d(TAG, "onResponse: body not null");
                                Log.d(TAG, "onResponse: " + response.body().items.size());
                                movies = response.body().items;
                            }else {
                                Log.d(TAG, "onResponse: body is null");
                                movies = Collections.emptyList();
                            }
                            successCallback.onSuccess(movies);
                        }else {
                            errorCallback.onError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                        Log.d(TAG, "onResponse: failed to get data.");
                        errorCallback.onError(t.getMessage());
                    }
                }
        );
    }

    public interface OnSuccessCallback {
        void onSuccess(List<Movie> movies);
    }
    public interface OnErrorCallback {
        void onError(String error);
    }
    public interface OnInsertFinishedCallback {
        void onInsertFinished();
    }
}
