// SettingActivity.java
package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

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

        ImageView backButton = findViewById(R.id.imageView32);
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

        recreate();
    }

    public void goBack(View view) {
        finish();
    }
}
