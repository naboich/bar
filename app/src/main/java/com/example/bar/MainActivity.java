package com.example.bar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bar.booking.BookingFragment;
import com.example.bar.diy.DIYFragment;
import com.example.bar.favorite.FavoriteFragment;
import com.example.bar.home.HomeFragment;
import com.example.bar.personal.PersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replace(new HomeFragment());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        replace(new HomeFragment());
                        break;
                    case R.id.diy:
                        replace(new DIYFragment());
                        break;
                    case R.id.personal:
                        replace(new PersonalFragment());
                        break;
                    case R.id.favorite:
                        replace(new FavoriteFragment());
                        break;
                    case R.id.booking:
                        replace(new BookingFragment());
                        break;
                }
                return true;
            }
        });

    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }
}