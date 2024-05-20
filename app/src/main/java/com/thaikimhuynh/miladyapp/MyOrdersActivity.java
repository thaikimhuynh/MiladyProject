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

import com.thaikimhuynh.miladyapp.fragment.CanceledFragment;
//import com.thaikimhuynh.miladyapp.fragment.ConfirmingFragment;
import com.thaikimhuynh.miladyapp.fragment.OnDeliveryFragment;
import com.thaikimhuynh.miladyapp.fragment.ReceivedFragment;

public class MyOrdersActivity extends AppCompatActivity {
    private TextView tabItem1, tabItem2, tabItem3, tabItem4;
    private static final int FRAGMENT_CONFIRMING = 1;
    private static final int FRAGMENT_ONDELIVERY = 2;
    private static final int FRAGMENT_RECEIVED = 3;
    private static final int FRAGMENT_CANCELED = 4;
    private int currentFragment = FRAGMENT_CONFIRMING;
    TextView selectedtextView;
    int tabNumber;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_orders);
        addViews();

//        Default fragment, default selected tab
        //replaceFragment(new ConfirmingFragment());

        tabItem1.setBackgroundResource(R.drawable.rounded_pink_button_background);
        tabItem1.setTextColor(Color.WHITE);

        tabItem2.setBackground(null);
        tabItem2.setTextColor(getResources().getColor(R.color.black));
        tabItem3.setBackground(null);
        tabItem3.setTextColor(getResources().getColor(R.color.black));
        tabItem4.setBackground(null);
        tabItem4.setTextColor(getResources().getColor(R.color.black));
        addEvents();

    }



    private void addEvents() {

//        tabItem1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                openConfirmFragment();
//                selectedtextView = tabItem1;
//                TextView[] non_selectedtextViews = {tabItem2, tabItem3, tabItem4};
//                tabNumber = 1;
//                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
//                translation(currentFragment, selectedtextView, non_selectedtextViews, slideTo);
//                currentFragment = tabNumber;
//
//            }
//        });
        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeliveryFragment();
                selectedtextView = tabItem2;
                TextView[] non_selectedtextViews = {tabItem1, tabItem3, tabItem4};

                tabNumber = 2;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();

                translation(currentFragment, selectedtextView, non_selectedtextViews, slideTo);
                currentFragment = tabNumber;





            }
        });
        tabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReceived();
                selectedtextView = tabItem3;
                TextView[] non_selectedtextViews = {tabItem1, tabItem2, tabItem4};
                tabNumber = 3;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
                translation(currentFragment, selectedtextView, non_selectedtextViews, slideTo);
                currentFragment = tabNumber;





            }
        });
        tabItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCanceledFragment();
                selectedtextView = tabItem4;
                TextView[] non_selectedtextViews = {tabItem1, tabItem2, tabItem3};

                tabNumber = 4;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();

                translation(currentFragment, selectedtextView, non_selectedtextViews, slideTo);
                currentFragment = tabNumber;







            }
        });
    }

    private void addViews() {
        tabItem1 = findViewById(R.id.tabItem1);
        tabItem2 = findViewById(R.id.tabItem2);
        tabItem3 = findViewById(R.id.tabItem3);
        tabItem4 = findViewById(R.id.tabItem4);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContainer_Order, fragment);
        transaction.commit();
    }

//    private void openConfirmFragment() {
//        if (currentFragment != FRAGMENT_CONFIRMING) {
//            replaceFragment(new ConfirmingFragment());
//        }
//
//
//    }

    private void onDeliveryFragment() {
        if (currentFragment != FRAGMENT_ONDELIVERY) {
            replaceFragment(new OnDeliveryFragment());
        }


    }
    private void openReceived() {
        if (currentFragment != FRAGMENT_RECEIVED) {
            replaceFragment(new ReceivedFragment());

        }


    }
    private void openCanceledFragment() {
        if (currentFragment != FRAGMENT_CANCELED) {
            replaceFragment(new CanceledFragment());

        }


    }

    private void translation(int selectedTabNumber, TextView selectedtextview, TextView[] nonSelectedTextViews, float slideTo) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);
        if (selectedTabNumber == FRAGMENT_CONFIRMING){
            tabItem1.startAnimation(translateAnimation);


        }
        else if (selectedTabNumber == FRAGMENT_ONDELIVERY)
        {
            tabItem2.startAnimation(translateAnimation);

        }
        else if (selectedTabNumber == FRAGMENT_RECEIVED)
        {
            tabItem3.startAnimation(translateAnimation);
        }
        else if (selectedTabNumber == FRAGMENT_CANCELED)
        {
            tabItem4.startAnimation(translateAnimation);

        }

            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedtextview.setBackgroundResource(R.drawable.rounded_pink_button_background);
                selectedtextview.setTextColor(Color.WHITE);
                for (int i = 0; i < nonSelectedTextViews.length; i++) {

                    nonSelectedTextViews[i].setBackground(null);
                    nonSelectedTextViews[i].setTextColor(getResources().getColor(R.color.black));


                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}