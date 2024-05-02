package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thaikimhuynh.miladyapp.R;

public class NavigationAdminActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPageAdapter viewPagerAdapter;
    private int tab1, tab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_admin);

        viewPager = findViewById(R.id.view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        viewPagerAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Clear existing menu items before inflating the new one
        bottomNavigationView.getMenu().clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu_navigation, bottomNavigationView.getMenu());

        tab1 = R.id.tab1;
        tab2 = R.id.tab2;

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        if (item.getItemId() == R.id.tab1) {
                            viewPager.setCurrentItem(0);
                            return true;
                        } else if (item.getItemId() == R.id.tab2) {
                            viewPager.setCurrentItem(1);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(tab1);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(tab2);
                        break;
                }
            }
        });

        // Set the initial selected item to tab1
        bottomNavigationView.setSelectedItemId(tab1);
    }
}
