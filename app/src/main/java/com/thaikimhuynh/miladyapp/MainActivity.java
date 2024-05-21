package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.thaikimhuynh.miladyapp.fragment.CartFragment;
import com.thaikimhuynh.miladyapp.fragment.CategoryFragment;
import com.thaikimhuynh.miladyapp.fragment.HomeFragment;
import com.thaikimhuynh.miladyapp.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_CATEGORY = 2;
    private static final int FRAGMENT_CART = 3;
    private static final int FRAGMENT_PROFILE = 4;

    private int currentFragment = FRAGMENT_HOME;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addViews();
        bottomNavigationView.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragment")) {
            String fragmentName = intent.getStringExtra("fragment");
            if (fragmentName != null) {
                openFragment(fragmentName);
            } else {
                openHomeFragment();
            }
        } else {
            openFragment("HomeFragment");
        }

        Log.d("MainActivity", "Current fragment: " + currentFragment);
    }

    private void addViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_home) {
            openHomeFragment();
        } else if (id == R.id.action_category) {
            openCategoryFragment();
        } else if (id == R.id.action_cart) {
            openCartFragment();
        } else if (id == R.id.action_profile) {
            openProfileFragment();
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }

    private void openFragment(String fragmentName) {
        Fragment fragment = null;
        switch (fragmentName) {
            case "ProfileFragment":
                fragment = new ProfileFragment();
                currentFragment = FRAGMENT_PROFILE;
                bottomNavigationView.setSelectedItemId(R.id.action_profile);
                break;
            // Add cases for other fragments if needed
            case "HomeFragment":
                fragment = new HomeFragment();
                currentFragment = FRAGMENT_HOME;
                bottomNavigationView.setSelectedItemId(R.id.action_home);
                break;
            case "CategoryFragment":
                fragment = new CategoryFragment();
                currentFragment = FRAGMENT_CATEGORY;
                bottomNavigationView.setSelectedItemId(R.id.action_category);
                break;
            case "CartFragment":
                fragment = new CartFragment();
                currentFragment = FRAGMENT_CART;
                bottomNavigationView.setSelectedItemId(R.id.action_cart);
                break;
        }

        if (fragment != null) {
            replaceFragment(fragment);
        }
    }

    private void openHomeFragment() {
        if (currentFragment != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment());
            currentFragment = FRAGMENT_HOME;
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    private void openCategoryFragment() {
        if (currentFragment != FRAGMENT_CATEGORY) {
            replaceFragment(new CategoryFragment());
            currentFragment = FRAGMENT_CATEGORY;
            bottomNavigationView.setSelectedItemId(R.id.action_category);
        }
    }

    private void openCartFragment() {
        if (currentFragment != FRAGMENT_CART) {
            CartFragment cartFragment = CartFragment.newInstance("param1", "param2");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_fragment, cartFragment)
                    .addToBackStack(null)
                    .commit();
            currentFragment = FRAGMENT_CART;
            bottomNavigationView.setSelectedItemId(R.id.action_cart);
        }
    }

    private void openProfileFragment() {
        if (currentFragment != FRAGMENT_PROFILE) {
            replaceFragment(new ProfileFragment());
            currentFragment = FRAGMENT_PROFILE;
            bottomNavigationView.setSelectedItemId(R.id.action_profile);
        }
    }
}
