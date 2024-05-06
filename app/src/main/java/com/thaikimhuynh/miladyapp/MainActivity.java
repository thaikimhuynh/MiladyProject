package com.thaikimhuynh.miladyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.thaikimhuynh.miladyapp.fragment.CartFragment;
import com.thaikimhuynh.miladyapp.fragment.CategoryFragment;
import com.thaikimhuynh.miladyapp.fragment.HomeFragment;
import com.thaikimhuynh.miladyapp.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener

{

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_CATEGORY = 2;
    private static final int FRAGMENT_CART = 3;
    private static final int FRAGMENT_PROFILE = 4;

    private int currentFragment = FRAGMENT_HOME;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addViews();
        replaceFragment(new HomeFragment());
        Log.d("MainActivity", "Current fragment: " + currentFragment); // Add this line
        bottomNavigationView.setOnItemSelectedListener(this);


    }

    private void addViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_home){
            openHomeFragment();

        } else if (id == R.id.action_category){
            openCategoryFragment();

        }
        else if (id == R.id.action_cart){
            openCartFragment();

        }
        else if (id == R.id.action_profile){
            openProfileFragment();

        }

        return true;
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }
    private void openHomeFragment(){
        if (currentFragment != FRAGMENT_HOME){
            replaceFragment(new HomeFragment());
            currentFragment = FRAGMENT_HOME;
        }


    }
    private void openCategoryFragment(){
        if (currentFragment != FRAGMENT_CATEGORY){
            replaceFragment(new CategoryFragment());
            currentFragment = FRAGMENT_CATEGORY;
        }


    }
//    private void openCartFragment(){
//        if (currentFragment != FRAGMENT_CART){
//            replaceFragment(new CartFragment());
//            currentFragment = FRAGMENT_CART;
//        }
//
//
//    }

    private void openCartFragment() {
        if (currentFragment != FRAGMENT_CART) {
            CartFragment cartFragment = CartFragment.newInstance("param1", "param2");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_fragment, cartFragment)
                    .addToBackStack(null)
                    .commit();
            currentFragment = FRAGMENT_CART;
        }
    }

    private void openProfileFragment(){
        if (currentFragment != FRAGMENT_PROFILE){
            replaceFragment(new ProfileFragment());
            currentFragment = FRAGMENT_PROFILE;
        }


    }
}