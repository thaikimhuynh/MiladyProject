package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adminfragment.CancledFragment;
import com.thaikimhuynh.miladyapp.adminfragment.ConfirmingFragment;
import com.thaikimhuynh.miladyapp.adminfragment.ReceivedFragment;
import com.thaikimhuynh.miladyapp.adminfragment.OnDeliveryFragment;
import com.thaikimhuynh.miladyapp.adminfragment.PreparedFragment;
import com.thaikimhuynh.miladyapp.login.WelcomeActivity;

public class AdminListOrderActivity extends AppCompatActivity {
    private TextView tabItem1_admin, tabItem2_admin, tabItem3_admin, tabItem4_admin, tabItem5_admin;
    private static final int FRAGMENT_CONFIRMING = 1;
    private static final int FRAGMENT_PREPARED = 2;
    private static final int FRAGMENT_ONDELIVERY = 3;
    private static final int FRAGMENT_RECEIVED = 4;

    private static final int FRAGMENT_CANCELED = 5;
    private int currentFragment = FRAGMENT_CONFIRMING;
    TextView selectedTextView;
    int tabNumber;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_order);
        addViews();
        replaceFragment(new ConfirmingFragment());

        tabItem1_admin.setBackgroundResource(R.drawable.rounded_pink_button_background);
        tabItem1_admin.setTextColor(Color.WHITE);
        tabItem2_admin.setBackground(null);
        tabItem2_admin.setTextColor(getResources().getColor(R.color.black));
        tabItem3_admin.setBackground(null);
        tabItem3_admin.setTextColor(getResources().getColor(R.color.black));
        tabItem4_admin.setBackground(null);
        tabItem4_admin.setTextColor(getResources().getColor(R.color.black));
        tabItem5_admin.setBackground(null);
        tabItem5_admin.setTextColor(getResources().getColor(R.color.black));
        addEvents();
        back=findViewById(R.id.left_arrow_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addEvents() {

        tabItem1_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmingFragment();
                selectedTextView = tabItem1_admin;
                TextView[] nonSelectedTextViews = {tabItem2_admin, tabItem3_admin, tabItem4_admin, tabItem5_admin};
                tabNumber = 1;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem2_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreparedFragment();
                selectedTextView = tabItem2_admin;
                TextView[] nonSelectedTextViews = {tabItem1_admin, tabItem3_admin, tabItem4_admin, tabItem5_admin};
                tabNumber = 2;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem3_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOnDeliveryFragment();
                selectedTextView = tabItem3_admin;
                TextView[] nonSelectedTextViews = {tabItem1_admin, tabItem2_admin, tabItem4_admin, tabItem5_admin};
                tabNumber = 3;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem4_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReceivedFragment();
                selectedTextView = tabItem4_admin;
                TextView[] nonSelectedTextViews = {tabItem1_admin, tabItem2_admin, tabItem3_admin, tabItem5_admin};
                tabNumber = 4;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem5_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCanceledFragment();
                selectedTextView = tabItem5_admin;
                TextView[] nonSelectedTextViews = {tabItem1_admin, tabItem2_admin, tabItem3_admin, tabItem4_admin};
                tabNumber = 5;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });
    }

    private void addViews() {
        tabItem1_admin = findViewById(R.id.tabItem1_admin);
        tabItem2_admin = findViewById(R.id.tabItem2_admin);
        tabItem3_admin = findViewById(R.id.tabItem3_admin);
        tabItem4_admin = findViewById(R.id.tabItem4_admin);
        tabItem5_admin = findViewById(R.id.tabItem5_admin);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContainer_Order, fragment);
        transaction.commit();
    }

    private void openConfirmingFragment() {
        if (currentFragment != FRAGMENT_CONFIRMING) {
            replaceFragment(new ConfirmingFragment());
        }
    }

    private void openPreparedFragment() {
        if (currentFragment != FRAGMENT_PREPARED) {
            replaceFragment(new PreparedFragment());
        }
    }

    private void openOnDeliveryFragment() {
        if (currentFragment != FRAGMENT_ONDELIVERY) {
            replaceFragment(new OnDeliveryFragment());
        }
    }

    private void openReceivedFragment() {
        if (currentFragment != FRAGMENT_RECEIVED) {
            replaceFragment(new ReceivedFragment());
        }
    }

    private void openCanceledFragment() {
        if (currentFragment != FRAGMENT_CANCELED) {
            replaceFragment(new CancledFragment());
        }
    }

    private void translation(int selectedTabNumber, TextView selectedtextview, TextView[] nonSelectedTextViews, float slideTo) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedtextview.setBackgroundResource(R.drawable.rounded_pink_button_background);
                selectedtextview.setTextColor(Color.WHITE);
                for (TextView textView : nonSelectedTextViews) {
                    textView.setBackground(null);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if (selectedTabNumber == FRAGMENT_CONFIRMING) {
            tabItem1_admin.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_PREPARED) {
            tabItem2_admin.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_ONDELIVERY) {
            tabItem3_admin.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_RECEIVED) {
            tabItem4_admin.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_CANCELED) {
            tabItem5_admin.startAnimation(translateAnimation);
        }}}