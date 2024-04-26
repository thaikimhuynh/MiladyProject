package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.thaikimhuynh.miladyapp.adminfragment.ProductFragment;
import com.thaikimhuynh.miladyapp.adminfragment.OrderFragment;

public class ViewPageAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductFragment();
            case 1:
                return new OrderFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
