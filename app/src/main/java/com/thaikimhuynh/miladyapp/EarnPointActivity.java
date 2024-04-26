package com.thaikimhuynh.miladyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.thaikimhuynh.miladyapp.fragment.CouponFragment;
import com.thaikimhuynh.miladyapp.fragment.RedeemPointsFragment;

public class EarnPointActivity extends AppCompatActivity {
    private TextView tabItem1, tabItem2;
    private static final int FRAGMENT_REDEEMPOINTS = 1;
    private static final int FRAGMENT_COUPON = 2;
    private int currentFragment = FRAGMENT_REDEEMPOINTS;
    TextView selectedtextView;
    TextView non_selectedtextView;
    int tabNumber;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_earn_point);
        addViews();

//        Default fragment, default selected tab
        replaceFragment(new RedeemPointsFragment());

        tabItem1.setBackgroundResource(R.drawable.rounded_pink_button_background);
        tabItem1.setTextColor(Color.WHITE);

        tabItem2.setBackground(null);
        tabItem2.setTextColor(getResources().getColor(R.color.pink_second));

        addEvents();

    }



    private void addEvents() {

        tabItem1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openRedeemPointFragment();
                selectedtextView = tabItem1;
                non_selectedtextView = tabItem2;
                tabNumber = 1;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
                translation(currentFragment, selectedtextView, non_selectedtextView, slideTo);
                currentFragment = tabNumber;

            }
        });
        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCouponFragment();
                selectedtextView = tabItem2;
                non_selectedtextView = tabItem1;
                tabNumber = 2;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();

                translation(currentFragment, selectedtextView, non_selectedtextView, slideTo);
                currentFragment = tabNumber;





            }
        });
    }

    private void addViews() {
        tabItem1 = findViewById(R.id.tabItem1);
        tabItem2 = findViewById(R.id.tabItem2);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContainer, fragment);
        transaction.commit();
    }

    private void openRedeemPointFragment() {
        if (currentFragment != FRAGMENT_REDEEMPOINTS) {
            replaceFragment(new RedeemPointsFragment());
        }


    }

    private void openCouponFragment() {
        if (currentFragment != FRAGMENT_COUPON) {
            replaceFragment(new CouponFragment());
        }


    }
    private void translation(int selectedTabNumber, TextView selectedtextview, TextView nonselectedtv, float slideTo) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);
        if (selectedTabNumber == FRAGMENT_REDEEMPOINTS){
            tabItem1.startAnimation(translateAnimation);


        }
        else
        {
            tabItem2.startAnimation(translateAnimation);

        }
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedtextview.setBackgroundResource(R.drawable.rounded_pink_button_background);
                selectedtextview.setTextColor(Color.WHITE);

                nonselectedtv.setBackground(null);
                nonselectedtv.setTextColor(getResources().getColor(R.color.pink_second));



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}