package com.example.android.movieexamprep.api;

import com.example.android.movieexamprep.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Data class to hold movie responses from API calls.
 */
public class MovieSearchResponse {
    @SerializedName("page")
    int page = 0;

    @SerializedName("total_pages")
    int total_pages = 0;

    @SerializedName("total_results")
    int total = 0;

    @SerializedName("results")
    List<Movie> items = Collections.emptyList();

}
