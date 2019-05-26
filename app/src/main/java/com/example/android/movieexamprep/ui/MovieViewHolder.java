package com.example.android.movieexamprep.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.movieexamprep.R;
import com.example.android.movieexamprep.model.Movie;
import com.example.android.movieviews.MovieRatingIndicator;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.android.movieexamprep.api.MovieServiceUtil.POSTER_PATH_BASE;

/**
 * View holder for a [Movie] RecyclerView list item.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private Movie movie;

    public TextView title;
    public TextView overview;
    public ImageView thumbnail;
    public MovieRatingIndicator indicator;

    public MovieViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        title = itemView.findViewById(R.id.movie_title);
        overview = itemView.findViewById(R.id.movie_overview);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        indicator = itemView.findViewById(R.id.moving_rating_indicator);
        this.context = context;
    }

    public static MovieViewHolder create(@NonNull ViewGroup parent) {
        Context ctx = parent.getContext();
        View view = LayoutInflater.from(ctx)
                .inflate(R.layout.movie_view_item, parent, false);
        return new MovieViewHolder(view, ctx);
    }

    public void bind(Movie movie) {
        if (movie == null) {
            title.setText(itemView.getResources().getString(R.string.loading));
            overview.setVisibility(View.GONE);
        } else {
            showMovieData(movie);
        }
    }

    private void showMovieData(Movie movie) {
        this.movie = movie;
        title.setText(movie.title);

        //If the rating is available show it!
        int descriptionVisibility = View.GONE;
        if (movie.voteAverage != 0) {
            indicator.setCurrentRating((int)movie.voteAverage*10);
            descriptionVisibility = View.VISIBLE;
        }
        indicator.setVisibility(descriptionVisibility);

        // If the overview is missing hide
        descriptionVisibility = View.GONE;
        if (movie.overview != null){
            overview.setText(movie.overview);
            descriptionVisibility = View.VISIBLE;
        }
        overview.setVisibility(descriptionVisibility);

        // Show the movie poster if available
        if (movie.posterPath != null) {
            Glide.with(context)
                    .load(POSTER_PATH_BASE + movie.posterPath)
                    .apply(new RequestOptions().placeholder(R.drawable.popcorn))
                    .into(thumbnail)
            ;
            thumbnail.setVisibility(View.VISIBLE);
        } else {
            thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.popcorn));
        }

    }

}
