package com.example.android.movieexamprep.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.android.movieexamprep.R;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch silenceNotificationsSwitch;
    private Button changeLanguageButton;

    public static final String SILENCE_NOTIFICATIONS_KEY = "silence_notifications";
    public static final String SHARED_PREF_FILE = "com.example.android.movieexamprep.sharedprefs";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean silenceNotifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        silenceNotifications = mSharedPreferences.getBoolean(SILENCE_NOTIFICATIONS_KEY, false);
        silenceNotificationsSwitch = (Switch) findViewById(R.id.switch_silence_notifications);
        silenceNotificationsSwitch.setChecked(silenceNotifications);
        silenceNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEditor.putBoolean(SILENCE_NOTIFICATIONS_KEY, b);
                mEditor.apply();
            }
        });

        changeLanguageButton = (Button) findViewById(R.id.button_language);

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
                startActivity(intent);
            }
        });
    }
}
