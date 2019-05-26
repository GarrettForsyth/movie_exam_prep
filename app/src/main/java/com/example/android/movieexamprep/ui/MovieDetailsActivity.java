package com.example.android.movieexamprep.ui;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.movieexamprep.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_TITLE = "extra_movie_title";
    public static final String EXTRA_MOVIE_OVERVIEW = "extra_movie_overview";
    public static final String EXTRA_MOVIE_POSTER_PATH = "extra_movie_poster_path";

    private ImageView thumbnail;
    private TextView title;
    private TextView overview;

    private String movieposterPath;
    private String movieTitle;
    private String movieOverview;

    private JobScheduler jobScheduler;
    private static int JOB_ID = 0;

    private SharedPreferences mSharedPreferences;
    private boolean silenceNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mSharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREF_FILE, MODE_PRIVATE);
        silenceNotifications = mSharedPreferences.getBoolean(SettingsActivity.SILENCE_NOTIFICATIONS_KEY, false);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        title = (TextView) findViewById(R.id.movie_title);
        overview = (TextView) findViewById(R.id.movie_overview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String movieTitle = extras.getString(EXTRA_MOVIE_TITLE, "No title available.");
            title.setText(movieTitle);
            this.movieTitle = movieTitle;

            String movieOverview = extras.getString(EXTRA_MOVIE_OVERVIEW, "No overview available.");
            overview.setText(movieOverview);
            this.movieOverview = movieOverview;

            String posterPath = extras.getString(EXTRA_MOVIE_POSTER_PATH,"");
            if (!posterPath.isEmpty()) {
                this.movieposterPath = posterPath;
                Glide.with(this)
                        .load(posterPath)
                        .into(thumbnail)
                ;
            }else {
                thumbnail.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.secondaryLightColor));
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleReminder(View view) {
        if (silenceNotifications){
            Toast.makeText(this, getString(R.string.notificions_on_silent), Toast.LENGTH_SHORT).show();
        }else {
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName serviceName = new ComponentName(this,
                    ReminderNotificationJobService.class);

            PersistableBundle bundle = new PersistableBundle();
            bundle.putString(EXTRA_MOVIE_TITLE, movieTitle);
            bundle.putString(EXTRA_MOVIE_OVERVIEW, movieOverview);
            bundle.putString(EXTRA_MOVIE_POSTER_PATH, movieposterPath);

            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                    .setExtras(bundle)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // be on wifi
                    .setMinimumLatency(1000 * 60 * 60 * 5); // wait at least 5 hours

            jobScheduler.schedule(builder.build());
            JOB_ID++; //increment the jobID
            showSnackbar();
        }
    }

    private void showSnackbar() {
        Snackbar mySnackbar = Snackbar.make(
                findViewById(R.id.frameLayout),
                R.string.reminder_set, Snackbar.LENGTH_INDEFINITE);

        mySnackbar.setActionTextColor(Color.WHITE);

        mySnackbar.setAction(getString(R.string.undo_string), new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                JOB_ID--; // use the last scheduled job.
                jobScheduler.cancel(JOB_ID);
            }
        });

        mySnackbar.show();
    }
}
