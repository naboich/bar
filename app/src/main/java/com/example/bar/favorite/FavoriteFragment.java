package com.example.bar.favorite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bar.R;
import com.google.android.material.tabs.TabLayout;

public class FavoriteFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FavoriteAdapter favoriteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager2 = root.findViewById(R.id.view_pager);
        favoriteAdapter = new FavoriteAdapter(getActivity());
        viewPager2.setAdapter(favoriteAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager2.setUserInputEnabled(false);
        // Inflate the layout for this fragment


        TextView click_add_diy = root.findViewById(R.id.click_add_diy);


        return root;
    }
}