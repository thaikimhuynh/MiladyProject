package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adminfragment.OrderFragment;
import com.thaikimhuynh.miladyapp.adminfragment.ProductFragment;

public class NavigationAdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private int tab1, tab2;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_admin);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        tab1 = R.id.tab1;
        tab2 = R.id.tab2;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;

                if (itemId == tab1) {
                    selectedFragment = new ProductFragment();
                } else if (itemId == tab2) {
                    selectedFragment = new OrderFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, false);
                    return true;
                }

                return false;
            }
        });

        // Load the initial fragment
        loadFragment(new ProductFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isInitialLoad) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isInitialLoad && isAppInitialized()) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    // Placeholder method for isAppInitialized()
    private boolean isAppInitialized() {
        // Implement your logic here to determine if the app is initialized
        return true; // For demonstration, always returning true
    }
}
