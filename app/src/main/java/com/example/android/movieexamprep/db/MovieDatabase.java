package com.example.android.movieexamprep.db;


import com.example.android.movieexamprep.model.Movie;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database schema that holds list of movies.
 * Uses singleton pattern to avoid multiple instances.
 */

@Database(entities = { Movie.class } , version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract com.example.android.movieexamprep.db.MovieDao movieDao();

}
