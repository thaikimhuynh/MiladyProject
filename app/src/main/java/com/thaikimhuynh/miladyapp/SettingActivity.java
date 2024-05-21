package com.thaikimhuynh.miladyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private AutoCompleteTextView languageDropdown;
    private AutoCompleteTextView aboutDropdown;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        languageDropdown = findViewById(R.id.autocompleteLan);
        aboutDropdown = findViewById(R.id.autocompleteAbout);

        setupLanguageDropdown();
        setupAboutDropdown();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }




    private void setupLanguageDropdown() {
        String[] languageOptions = getResources().getStringArray(R.array.language_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, languageOptions);
        languageDropdown.setAdapter(adapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentLanguage = sharedPreferences.getString("language_preference", "English");
        languageDropdown.setText(currentLanguage, false);

        languageDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLanguage = (String) parent.getItemAtPosition(position);
            updateAppLanguage(selectedLanguage);
        });
    }

    private void setupAboutDropdown() {
        String[] aboutOptions = {"v2.2024.10.5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, aboutOptions);
        aboutDropdown.setAdapter(adapter);
        aboutDropdown.setText(aboutOptions[0], false);
    }

    private void updateAppLanguage(String language) {
        String languageCode = "en"; // default to English
        if (language.equals("Vietnamese")) {
            languageCode = "vi";
        }

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("language_preference", language);
        editor.apply();


        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog_restart, null);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        Button restartButton = dialogView.findViewById(R.id.btn_restart);

        titleTextView.setText(R.string.restart_title);
        messageTextView.setText(R.string.restart_message);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }


}
