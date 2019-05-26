package com.example.android.movieexamprep.db;


import com.example.android.movieexamprep.model.Movie;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Room data access object for accessing the [Movie] table.
 */
@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Movie> movies);

    /*
        Queries the API based on a string query parameter.
     */
    @Query("SELECT * FROM movies " +
            "WHERE title LIKE :queryString" +
            " OR overview LIKE :queryString")
    DataSource.Factory<Integer, Movie> moviesByQueryString(String queryString);

    /*
        Queries the API based on a string query parameter.
        Filters by language
     */
    @Query("SELECT * FROM movies" +
            " WHERE (title LIKE :queryString)" +
            " OR (overview LIKE :queryString)" +
            " ORDER BY originalLangauge LIKE :language DESC,voteAverage DESC, popularity DESC")
    DataSource.Factory<Integer, Movie> moviesByQueryStringFilterLanguage(String queryString, String language);
}
