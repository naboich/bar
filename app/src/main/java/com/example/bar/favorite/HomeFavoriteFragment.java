package com.example.bar.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.homemodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFavoriteFragment extends Fragment {

    RecyclerView HomeFavoriteFragment_rv;
    HomeFavoriteFragmentAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_favorite, container, false);

        HomeFavoriteFragment_rv = root.findViewById(R.id.HomeFavoriteFragment_rv);
        HomeFavoriteFragment_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<homemodel> options =
                new FirebaseRecyclerOptions.Builder<homemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("favorite_home"), homemodel.class)
                        .build();

        adapter = new HomeFavoriteFragmentAdapter(options);
        adapter.startListening();
        HomeFavoriteFragment_rv.setAdapter(adapter);

        TextView click_add_bar = root.findViewById(R.id.click_add_bar);
        click_add_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}