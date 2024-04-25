package com.thaikimhuynh.miladyapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.thaikimhuynh.miladyapp.fragment.PromotionFragment;
import com.thaikimhuynh.miladyapp.fragment.YoursFragment;
public class NotificationActivity extends AppCompatActivity {
    private TextView tabItem_Promotion, tabItem_Yours;
    private static final int FRAGMENT_PROMOTION = 1;
    private static final int FRAGMENT_YOURS = 2;
    private int currentFragment = FRAGMENT_PROMOTION;
    TextView selectedtextView;
    TextView non_selectedtextView;
    int tabNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        addViews();

        // Default fragment, default selected tab
        replaceFragment(new PromotionFragment());

        tabItem_Promotion.setBackgroundResource(R.drawable.rounded_pink_button_background);
        tabItem_Promotion.setTextColor(Color.WHITE);

        tabItem_Yours.setBackground(null);
        tabItem_Yours.setTextColor(getResources().getColor(R.color.pink_second));

        addEvents();
    }

    private void addEvents() {
        tabItem_Promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPromotionFragment();
                selectedtextView = tabItem_Promotion;
                non_selectedtextView = tabItem_Yours;
                tabNumber = 1;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
                translation(currentFragment, selectedtextView, non_selectedtextView, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem_Yours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoursFragment();
                selectedtextView = tabItem_Yours;
                non_selectedtextView = tabItem_Promotion;
                tabNumber = 2;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
                translation(currentFragment, selectedtextView, non_selectedtextView, slideTo);
                currentFragment = tabNumber;
            }
        });
    }

    private void addViews() {
        tabItem_Promotion = findViewById(R.id.tabItem_Promotion);
        tabItem_Yours = findViewById(R.id.tabItem_Yours);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentNotification, fragment);
        transaction.commit();
    }

    private void openPromotionFragment() {
        if (currentFragment != FRAGMENT_PROMOTION) {
            replaceFragment(new PromotionFragment());
        }
    }

    private void openYoursFragment() {
        if (currentFragment != FRAGMENT_YOURS) {
            replaceFragment(new YoursFragment());
        }
    }

    private void translation(int selectedTabNumber, TextView selectedtextview, TextView nonselectedtv, float slideTo) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);
        if (selectedTabNumber == FRAGMENT_PROMOTION){
            tabItem_Promotion.startAnimation(translateAnimation);
        } else {
            tabItem_Yours.startAnimation(translateAnimation);
        }
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedtextview.setBackgroundResource(R.drawable.rounded_pink_button_background);
                selectedtextview.setTextColor(Color.WHITE);
                nonselectedtv.setBackground(null);
                nonselectedtv.setTextColor(getResources().getColor(R.color.pink_second));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
