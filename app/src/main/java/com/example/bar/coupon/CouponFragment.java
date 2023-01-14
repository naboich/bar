package com.example.bar.coupon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bar.R;
import com.example.bar.favorite.HomeFavoriteFragmentAdapter;
import com.example.bar.home.HomeAdapter;
import com.example.bar.homemodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CouponFragment extends Fragment {

    RecyclerView coupon_rv;
    CouponAdpter couponAdpter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("coupon");
    ArrayList<homemodel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coupon, container, false);

        coupon_rv = root.findViewById(R.id.coupon_rv);
        coupon_rv.setHasFixedSize(true);
        coupon_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        couponAdpter = new CouponAdpter(getContext(), list);
        coupon_rv.setAdapter(couponAdpter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    homemodel bar = dataSnapshot.getValue(homemodel.class);
                    list.add(bar);
                }
                couponAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }


}