package com.example.bar.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.diy.DIYBaseAdapter;
import com.example.bar.diymodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DIYFavoriteFragment extends Fragment {

    RecyclerView DIYFavoriteFragment_rv;
    DIYFavoriteFragmentAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView click_add_diy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_d_i_y_favorite, container, false);

        DIYFavoriteFragment_rv = root.findViewById(R.id.DIYFavoriteFragment_rv);
        DIYFavoriteFragment_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<diymodel> options =
                new FirebaseRecyclerOptions.Builder<diymodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("favorite_diy"), diymodel.class)
                        .build();

        adapter = new DIYFavoriteFragmentAdapter(options);
        adapter.startListening();
        DIYFavoriteFragment_rv.setAdapter(adapter);

        //
        click_add_diy = root.findViewById(R.id.click_add_diy);
        click_add_diy.setOnClickListener(new View.OnClickListener() {
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