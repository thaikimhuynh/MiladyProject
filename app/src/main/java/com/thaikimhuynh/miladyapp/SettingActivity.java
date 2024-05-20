package com.thaikimhuynh.miladyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    String[] item = {"English", "Vietnamese"};
    String[] versions = {"v2.2024.5.10"};

    AutoCompleteTextView autoCompleteTextViewLan;
    AutoCompleteTextView autoCompleteTextViewAbout;

    ArrayAdapter<String> adapterLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, versions);
        autoCompleteTextViewAbout = findViewById(R.id.autocompleteAbout);
        autoCompleteTextViewAbout.setAdapter(adapter);

        autoCompleteTextViewLan = findViewById(R.id.autocompleteLan);
        adapterLanguage = new ArrayAdapter<>(this, R.layout.list_language, item);
        autoCompleteTextViewLan.setAdapter(adapterLanguage);

        autoCompleteTextViewLan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(SettingActivity.this, "You chose " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }


//Quay về trang trước
    public void goBack(View view) {
        finish();
    }
}
