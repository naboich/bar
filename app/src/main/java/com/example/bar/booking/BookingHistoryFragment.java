package com.example.bar.booking;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bar.R;
import com.example.bar.home.HomeAdapter;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BookingHistoryFragment extends Fragment {

    RecyclerView booking_history_rv;
    BookingHistoryAdapter bookingHistoryAdapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("booking_history");

    ArrayList<homemodel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_booking_history, container, false);

        booking_history_rv = root.findViewById(R.id.booking_history_rv);
        booking_history_rv.setHasFixedSize(true);
        booking_history_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        bookingHistoryAdapter = new BookingHistoryAdapter(getContext(), list);
        booking_history_rv.setAdapter(bookingHistoryAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    homemodel bar = dataSnapshot.getValue(homemodel.class);
                    list.add(bar);
                }
                bookingHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button clear_btn = root.findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                    Toast.makeText(getContext(), "尚未有紀錄", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("確定要移除紀錄嗎?");
                    builder.setMessage("刪除後將無法復原");
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.removeValue();
                            list.clear();
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                                    Toast.makeText(view.getContext(), "已取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            }
        });

        return root;
    }
}