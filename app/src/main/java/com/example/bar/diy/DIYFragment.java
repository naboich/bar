package com.example.bar.diy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bar.R;
import com.google.android.material.tabs.TabLayout;

public class DIYFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    DIYTabs diyTabs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_d_i_y, container, false);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager2 = root.findViewById(R.id.view_pager);
        diyTabs = new DIYTabs(this);
        viewPager2.setAdapter(diyTabs);
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
        return root;
    }
}