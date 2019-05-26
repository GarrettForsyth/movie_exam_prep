package com.example.android.movieexamprep.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.movieexamprep.R;
import com.example.android.movieexamprep.model.Movie;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static com.example.android.movieexamprep.api.MovieServiceUtil.POSTER_PATH_BASE;
import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_OVERVIEW;
import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_POSTER_PATH;
import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_TITLE;

/**
 * Adapter for list of movies.
 */
public class MovieAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private Activity activity;

    protected MovieAdapter(Activity activity) {
        super(DIFF_CALLBACK);
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MovieViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Movie movieItem = getItem(position);
        if (movieItem != null) {
            ((MovieViewHolder) viewHolder).bind(movieItem);
            viewHolder.itemView.setOnClickListener(view -> {
                Intent detailsIntent = new Intent(activity ,MovieDetailsActivity.class)
                        .putExtra(EXTRA_MOVIE_TITLE, movieItem.title)
                        .putExtra(EXTRA_MOVIE_OVERVIEW, movieItem.overview);
                if (movieItem.posterPath != null && !movieItem.posterPath.isEmpty()) {
                            detailsIntent.putExtra(EXTRA_MOVIE_POSTER_PATH, POSTER_PATH_BASE + movieItem.posterPath);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Pair<View, String> p1 = Pair.create(
                             ((MovieViewHolder) viewHolder).thumbnail,
                            activity.getResources().getString(R.string.transition_name_thumbnail));
                    ActivityOptionsCompat options = makeSceneTransitionAnimation(activity, p1);
                    activity.startActivity(detailsIntent, options.toBundle());
                }else {
                    activity.startActivity(detailsIntent);
                }
            });
        }
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                // Movie details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Movie oldMovie, Movie newMovie) {
                    Log.d("TESTDBUG", "areItemsTheSame: " + " testing "  + oldMovie.getId() + " and " + newMovie.getId());
                    return oldMovie.getId().equals(newMovie.getId());
                }

                @Override
                public boolean areContentsTheSame(Movie oldMovie,
                                                  Movie newMovie) {
                    return oldMovie.equals(newMovie);
                }
            };
}
