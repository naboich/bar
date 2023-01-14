package com.example.bar.favorite;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FavoriteAdapter extends FragmentStateAdapter {
    public FavoriteAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFavoriteFragment();
            case 1:
                return new DIYFavoriteFragment();
            default:
                return new HomeFavoriteFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
