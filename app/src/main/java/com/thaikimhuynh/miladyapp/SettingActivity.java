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

        // Thiết lập adapter cho autoCompleteTextViewLan sử dụng đối tượng Language
        adapterLanguage = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, item);
        autoCompleteTextViewLan.setAdapter(adapterLanguage);

        autoCompleteTextViewLan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();

                // Tạo một đối tượng Locale với ngôn ngữ được chọn
                Locale locale;
                if (selectedLanguage.equals("Vietnamese")) {
                    locale = new Locale("vi"); // Mã ngôn ngữ cho tiếng Việt
                } else {
                    locale = new Locale("en"); // Mã ngôn ngữ cho tiếng Anh (mặc định)
                }

                // Đặt ngôn ngữ cho ứng dụng
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                // Lưu ngôn ngữ được chọn vào SharedPreferences để giữ cho thiết lập
                SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("My_Lang", locale.getLanguage());
                editor.apply();

                // Hiển thị thông báo khi chọn ngôn ngữ
                Toast.makeText(SettingActivity.this, "You chose " + selectedLanguage, Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
    }

    // Quay về trang trước
    public void goBack(View view) {
        finish();
    }

    // Lớp để đại diện cho ngôn ngữ
    private static class Language {
        private String name;
        private String code;

        public Language(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }
    }
}
