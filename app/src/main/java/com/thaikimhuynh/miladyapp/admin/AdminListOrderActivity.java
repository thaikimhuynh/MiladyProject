package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adminfragment.CancledFragment;
import com.thaikimhuynh.miladyapp.adminfragment.ConfirmingFragment;
import com.thaikimhuynh.miladyapp.adminfragment.ReceivedFragment;
import com.thaikimhuynh.miladyapp.adminfragment.RefundedFragment;
import com.thaikimhuynh.miladyapp.adminfragment.OnDeliveryFragment;
import com.thaikimhuynh.miladyapp.adminfragment.PreparedFragment;

public class AdminListOrderActivity extends AppCompatActivity {
    private TextView tabItem1, tabItem2, tabItem3, tabItem4, tabItem5, tabItem6;
    private static final int FRAGMENT_CONFIRMING = 1;
    private static final int FRAGMENT_PREPARED = 2;
    private static final int FRAGMENT_ONDELIVERY = 3;
    private static final int FRAGMENT_RECEIVED = 4;
    private static final int FRAGMENT_REFUNDED = 5;
    private static final int FRAGMENT_CANCELED = 6;
    private int currentFragment = FRAGMENT_CONFIRMING;
    TextView selectedTextView;
    int tabNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_order);
        addViews();
        replaceFragment(new ConfirmingFragment());

        tabItem1.setBackgroundResource(R.drawable.rounded_pink_button_background);
        tabItem1.setTextColor(Color.WHITE);
        tabItem2.setBackground(null);
        tabItem2.setTextColor(getResources().getColor(R.color.black));
        tabItem3.setBackground(null);
        tabItem3.setTextColor(getResources().getColor(R.color.black));
        tabItem4.setBackground(null);
        tabItem4.setTextColor(getResources().getColor(R.color.black));
        tabItem5.setBackground(null);
        tabItem5.setTextColor(getResources().getColor(R.color.black));
        tabItem6.setBackground(null);
        tabItem6.setTextColor(getResources().getColor(R.color.black));
        addEvents();
    }

    private void addEvents() {
        tabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmingFragment();
                selectedTextView = tabItem1;
                TextView[] nonSelectedTextViews = {tabItem2, tabItem3, tabItem4, tabItem5, tabItem6};
                tabNumber = 1;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreparedFragment();
                selectedTextView = tabItem2;
                TextView[] nonSelectedTextViews = {tabItem1, tabItem3, tabItem4, tabItem5, tabItem6};
                tabNumber = 2;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOnDeliveryFragment();
                selectedTextView = tabItem3;
                TextView[] nonSelectedTextViews = {tabItem1, tabItem2, tabItem4, tabItem5, tabItem6};
                tabNumber = 3;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReceivedFragment();
                selectedTextView = tabItem4;
                TextView[] nonSelectedTextViews = {tabItem1, tabItem2, tabItem3, tabItem5, tabItem6};
                tabNumber = 4;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRefundedFragment();
                selectedTextView = tabItem5;
                TextView[] nonSelectedTextViews = {tabItem1, tabItem2, tabItem3, tabItem4, tabItem6};
                tabNumber = 5;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });

        tabItem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCanceledFragment();
                selectedTextView = tabItem6;
                TextView[] nonSelectedTextViews = {tabItem1, tabItem2, tabItem3, tabItem4, tabItem5};
                tabNumber = 6;
                float slideTo = (tabNumber - currentFragment) * selectedTextView.getWidth();
                translation(currentFragment, selectedTextView, nonSelectedTextViews, slideTo);
                currentFragment = tabNumber;
            }
        });
    }

    private void addViews() {
        tabItem1 = findViewById(R.id.tabItem1);
        tabItem2 = findViewById(R.id.tabItem2);
        tabItem3 = findViewById(R.id.tabItem3);
        tabItem4 = findViewById(R.id.tabItem4);
        tabItem5 = findViewById(R.id.tabItem5);
        tabItem6 = findViewById(R.id.tabItem6);
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

    private void openRefundedFragment() {
        if (currentFragment != FRAGMENT_REFUNDED) {
            replaceFragment(new RefundedFragment());
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
        if (selectedTabNumber == FRAGMENT_CONFIRMING) {
            tabItem1.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_PREPARED) {
            tabItem2.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_ONDELIVERY) {
            tabItem3.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_RECEIVED) {
            tabItem4.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_REFUNDED) {
            tabItem5.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_CANCELED) {
            tabItem6.startAnimation(translateAnimation);
        }

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
    }
}