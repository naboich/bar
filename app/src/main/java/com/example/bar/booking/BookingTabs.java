package com.example.bar.booking;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bar.diy.DIYBaseFragment;
import com.example.bar.diy.DIYProFragment;

public class BookingTabs  extends FragmentStateAdapter {
    public BookingTabs(@NonNull BookingFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new BookingNowFragment();
            case 1:
                return new BookingHistoryFragment();
            default:
                return new BookingNowFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


