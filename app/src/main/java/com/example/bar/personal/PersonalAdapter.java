package com.example.bar.personal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bar.favorite.DIYFavoriteFragment;
import com.example.bar.favorite.HomeFavoriteFragment;

public class PersonalAdapter extends FragmentStateAdapter {
    public PersonalAdapter(@NonNull PersonalFragment fragmentActivity) {
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
