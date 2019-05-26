package com.example.android.movieexamprep.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {


    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMoviesByStringQuery(
            @Query("query") String query,
            @Query("page") int page,
            @Query("per_page") int itemsPerPage
    );

}

