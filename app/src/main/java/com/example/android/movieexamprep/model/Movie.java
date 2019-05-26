package com.example.android.movieexamprep.model;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Immutable model class for a movie that holds all
 * information about a movie. This data is received
 * by TheMovieDb Api so all fields are annotated
 * with the serialized name.
 */

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    public String title;
    @SerializedName("overview")
    public String overview;
    //@SerializedName("genre_ids") public int[] genreIds;
    @SerializedName("original_language")
    public String originalLangauge;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("popularity")
    public float popularity;
    @SerializedName("vote_average")
    public float voteAverage;
    @SerializedName("poster_path")
    public String posterPath;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie(
            Long id,
            String title,
            String overview,
            String originalLanguage,
            float voteAverage,
            String posterPath
    ) {
        this(
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

    public Movie(
            Long id,
            String title,
            String overview,
            String originalLangauge,
            String releaseDate,
            float popularity,
            float voteAverage,
            String posterPath
    ) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.originalLangauge = originalLangauge;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        else if (o instanceof Movie) {
            Movie p = (Movie)o;
            return this.id.equals(p.id);
        }
        return false;
    }
}
