package com.example.android.movieexamprep.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieexamprep.R;
import com.example.android.movieexamprep.di.Injectable;
import com.example.android.movieexamprep.model.Movie;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements Injectable {

    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "The Godfather";

    static final String NOTIFICATION_CHANNEL_ID =
            "android.example.com.movieexamprep.NOTIFICATION_CHANNEL_ID";

    @Inject
    MovieViewModel viewModel;

    private MovieAdapter adapter;
    private RecyclerView recyclerView;

    private TextView emptyList;
    private EditText searchMovie;

    private String currentLanguage;

    private ProgressBar progressBar;


    @Inject
    String testInjectionObject = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();

        progressBar = findViewById(R.id.spinner);
        progressBar.setVisibility(View.GONE);

        emptyList = (TextView) findViewById(R.id.emptyList);
        searchMovie = (EditText) findViewById(R.id.search_movie);

        if(testInjectionObject != null) {
            Toast.makeText(this, testInjectionObject, Toast.LENGTH_LONG).show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        int numberOfColumns = getResources().getInteger(R.integer.number_of_grid_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        initializeAdapter();

        // add dividers between RecyclerView's row items
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        String query = "";
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LAST_SEARCH_QUERY, "");
        }

        String deviceLocale = Locale.getDefault().getCountry();
        switch (deviceLocale) {
            case "IT":
                currentLanguage="it";
                break;
            case "DE":
                currentLanguage="de";
                break;
            default:
                currentLanguage="en";

        }

        //viewModel.searchMovie(query, currentLanguage);

        initSearch(query);
    }

    private void initSearch(String query) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        searchMovie.setText(query);
        searchMovie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_GO) {
                    updateMovieListFromInput();
                    View view = getCurrentFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }else {
                    return false;
                }
            }
        });
        searchMovie.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateMovieListFromInput();
                    view = getCurrentFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    private void updateMovieListFromInput() {
        progressBar.setVisibility(View.VISIBLE);
        String query = searchMovie.getText().toString().trim();
        if (!query.isEmpty()) {
            recyclerView.scrollToPosition(0);
            viewModel.searchMovie(query, currentLanguage);
            adapter.submitList(null);
        }
    }

    private void initializeAdapter() {
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel.getMovies().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movie> movies) {
                Log.d("MovieCount", "MainActivity::InitializeAdapter onChange Observable  " + movies.size());
                progressBar.setVisibility(View.GONE);
                showEmptyList(movies.size() == 0);
                Log.d("TESTDEBUG", "onChanged: " + movies.toString());
                adapter.submitList(movies);
            }
        });
        viewModel.getNetworkErrors().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(getBaseContext(), "\uD83D\uDE28 Whoops" + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showEmptyList(boolean show) {
        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            emptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
