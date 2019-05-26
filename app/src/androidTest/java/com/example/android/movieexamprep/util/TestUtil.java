package com.example.android.movieexamprep.util;

import com.example.android.movieexamprep.model.Movie;

public class TestUtil {


    public static Movie createMovie(
            Long id,
            String title,
            String overview,
            String originalLanguage,
            float voteAverage,
            String posterPath
    ) {
        return createMovie(
                id,
                title,
                overview,
                originalLanguage,
                null,
                -1,
                voteAverage,
                posterPath
        );
    }

    public static Movie createMovie(
            Long id,
            String title,
            String overview,
            String originalLanguage,
            String releaseDate,
            float popularity,
            float voteAverage,
            String posterPath
    ) {
        return new Movie(
                id,
                title,
                overview,
                originalLanguage,
                releaseDate,
                popularity,
                voteAverage,
                posterPath
        );
    }
}
