package com.example.android.movieexamprep.util;

import android.util.Log;

import com.example.android.movieexamprep.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

public class FakeMovieDataSource extends ItemKeyedDataSource<Integer, Movie> {

    private static final String TAG = FakeMovieDataSource.class.getSimpleName();
    private static int MAX_PAGE_SIZE = 20;

    /**
     * List of items in the mock data source.
     */
    private ArrayList<Movie> fakeMovies = new ArrayList<>();

    {
        Log.d(TAG, "instance initializer: creating fake movies..");
        Random rand = new Random();
        for(long i = 0; i < 200; i++) {
            fakeMovies.add(
                    TestUtil.createMovie(
                            i,
                            "fake title " + i,
                            "fake overview " + i,
                            "en",
                            rand.nextInt(11),
                            null
                    )
            );
        }
    }

    private int  inRange(int position, int start, int end) {
        if (position < start) return start;
        if (position > end)   return  end;
        return position;
    }

    /**
     * Load initial data
     * @param params
     * @param callback
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Movie> callback) {
        Log.d(TAG, "loadInitial: requesting  start " + params.requestedInitialKey + " and size " + params.requestedLoadSize);
        int position = params.requestedInitialKey == null ? 0 : params.requestedInitialKey;

        int pageSize = Math.min(MAX_PAGE_SIZE, params.requestedLoadSize);
        int firstItem = inRange(position,0, fakeMovies.size());
        int lastItem = inRange(position + pageSize, 0,  fakeMovies.size());

        Log.d(TAG, "loadInitial: firstItem = " + firstItem + " lastItem = " + lastItem);

        List<Movie> data;
        if (firstItem == lastItem) data = new ArrayList<>();
        else                       data = fakeMovies.subList(firstItem, lastItem);
        if (params.placeholdersEnabled) {
            callback.onResult(data, firstItem, fakeMovies.size());
        }else {
            callback.onResult(data);
        }

    }

    /**
     * Load next page
     * @param params
     * @param callback
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Movie> callback) {
        Log.d(TAG, "loadAfter: requesting  key " + params.key + " and size " + params.requestedLoadSize);

        int pageSize = Math.min(MAX_PAGE_SIZE, params.requestedLoadSize);
        int firstItem = inRange(params.key + 1,0, fakeMovies.size());
        int lastItem = inRange(firstItem + pageSize, 0,  fakeMovies.size());

        Log.d(TAG, "loadInitial: firstItem = " + firstItem + " lastItem = " + lastItem);

        List<Movie> data;
        if (firstItem == lastItem) data = new ArrayList<>();
        else                       data = fakeMovies.subList(firstItem, lastItem);
        callback.onResult(data);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Movie> callback) {
        Log.d(TAG, "loadBefore: requesting  key " + params.key + " and size " + params.requestedLoadSize);

        int pageSize = Math.min(MAX_PAGE_SIZE, params.requestedLoadSize);
        int firstItem = inRange(params.key - 1,0, fakeMovies.size());
        int lastItem = inRange(firstItem + pageSize, 0,  fakeMovies.size());

        Log.d(TAG, "loadInitial: firstItem = " + firstItem + " lastItem = " + lastItem);

        List<Movie> data;
        if (firstItem == lastItem) data = new ArrayList<>();
        else                       data = fakeMovies.subList(firstItem, lastItem);
        callback.onResult(data);
    }

    @NonNull
    @Override
    public Integer getKey(@NonNull Movie item) {
        return fakeMovies.indexOf(item);
    }

    /**
     * Save a new item to the list
     */
    void saveItem(Movie item) {
        Log.d(TAG, "saveItem: saving new movie with id: " + item.getId());
        int index = getKey(item);
        if(index < 0){
            fakeMovies.add(item);
        } else{
            fakeMovies.set(index, item);
        }
        invalidate();
    }
}
