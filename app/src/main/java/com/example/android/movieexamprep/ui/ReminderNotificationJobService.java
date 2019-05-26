package com.example.android.movieexamprep.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PersistableBundle;

import com.example.android.movieexamprep.R;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_OVERVIEW;
import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_POSTER_PATH;
import static com.example.android.movieexamprep.ui.MovieDetailsActivity.EXTRA_MOVIE_TITLE;
import static com.example.android.movieexamprep.ui.SettingsActivity.SHARED_PREF_FILE;
import static com.example.android.movieexamprep.ui.SettingsActivity.SILENCE_NOTIFICATIONS_KEY;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ReminderNotificationJobService extends JobService {

    private static final int NOTIFICATION_REQUEST_CODE = 1;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        boolean silenceNotifications = sharedPreferences.getBoolean(SILENCE_NOTIFICATIONS_KEY, false);

        if (silenceNotifications) return false; // return immediately if silence notifications is on

        // create intent to launch MovieDetailsActivity.class when clicked
        Intent intent = new Intent(this, MovieDetailsActivity.class);

        PersistableBundle extras = jobParameters.getExtras();
        // include the available details
        String title = extras.getString(EXTRA_MOVIE_TITLE);
        String overview = extras.getString(EXTRA_MOVIE_OVERVIEW);
        String posterPath = extras.getString(EXTRA_MOVIE_POSTER_PATH);
        intent.putExtra(EXTRA_MOVIE_TITLE, title);
        intent.putExtra(EXTRA_MOVIE_OVERVIEW, overview);
        intent.putExtra(EXTRA_MOVIE_POSTER_PATH, posterPath);

        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(overview)
                        .setContentIntent(contentPendingIntent)
                        .setSmallIcon(R.drawable.popcorn_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setAutoCancel(true);

        nm.notify(NOTIFICATION_REQUEST_CODE, builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
