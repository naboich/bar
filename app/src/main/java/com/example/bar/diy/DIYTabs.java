package com.example.bar.diy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DIYTabs extends FragmentStateAdapter {

    public DIYTabs(@NonNull DIYFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DIYBaseFragment();
            case 1:
                return new DIYProFragment();
            default:
                return new DIYBaseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
