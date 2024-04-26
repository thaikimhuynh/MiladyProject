package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adminfragment.BootsFragment;
import com.thaikimhuynh.miladyapp.adminfragment.HeelsFragment;
import com.thaikimhuynh.miladyapp.adminfragment.SandalsFragment;
import com.thaikimhuynh.miladyapp.adminfragment.SneakersFragment;
public class AdminListProductActivity extends AppCompatActivity {

    private TextView tabItem1, tabItem2, tabItem3, tabItem4;
    private static final int FRAGMENT_BOOTS = 1;
    private static final int FRAGMENT_HEELS = 2;
    private static final int FRAGMENT_SANDALS = 3;
    private static final int FRAGMENT_SNEAKERS = 4;
    private int currentFragment = FRAGMENT_BOOTS;
    TextView selectedtextView;
    int tabNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_product);
        addViews();
        replaceFragment(new BootsFragment());

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
        tabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBootsFragment();
                selectedtextView = tabItem1;
                TextView[] non_selectedtextViews = {tabItem2, tabItem3, tabItem4};
                tabNumber = 1;
                float slideTo = (tabNumber - currentFragment) * selectedtextView.getWidth();
                translation(currentFragment, selectedtextView, non_selectedtextViews, slideTo);
                currentFragment = tabNumber;
            }
        });
        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeelsFragment();
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
                openSandalsFragment();
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
                openSneakersFragment();
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

    private void openBootsFragment() {
        if (currentFragment != FRAGMENT_BOOTS) {
            replaceFragment(new BootsFragment());
        }
    }

    private void openHeelsFragment() {
        if (currentFragment != FRAGMENT_HEELS) {
            replaceFragment(new HeelsFragment());
        }
    }

    private void openSandalsFragment() {
        if (currentFragment != FRAGMENT_SANDALS) {
            replaceFragment(new SandalsFragment());
        }
    }

    private void openSneakersFragment() {
        if (currentFragment != FRAGMENT_SNEAKERS) {
            replaceFragment(new SneakersFragment());
        }
    }

    private void translation(int selectedTabNumber, TextView selectedtextview, TextView[] nonSelectedTextViews, float slideTo) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, slideTo, 0, 0);
        translateAnimation.setDuration(100);
        if (selectedTabNumber == FRAGMENT_BOOTS){
            tabItem1.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_HEELS) {
            tabItem2.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_SANDALS) {
            tabItem3.startAnimation(translateAnimation);
        } else if (selectedTabNumber == FRAGMENT_SNEAKERS) {
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