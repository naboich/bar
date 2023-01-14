package com.example.bar.coupon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.R;
import com.example.bar.favorite.DIYFavoriteFragmentAdapter;
import com.example.bar.home.HomeAdapter;
import com.example.bar.homemodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CouponAdpter extends RecyclerView.Adapter<CouponAdpter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CouponAdpter(android.content.Context context, ArrayList<homemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_item, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        homemodel bar = list.get(position);
        DatabaseReference coupon_databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("coupon").child(String.valueOf(bar.getId()));

        holder.coupon_title.setText(bar.getCoupon_title());
        holder.coupon_discount.setText(bar.getCoupon_discount());
        holder.coupon_serial_number.setText(bar.getCoupon_serial_number());
        holder.coupon_low_consumption.setText(bar.getCoupon_low_consumption());
        holder.coupon_effective_date.setText(bar.getYear() + "/" + bar.getMonth() + "/" + bar.getDay());
        holder.coupon_from.setText(bar.getCoupon_from());

        //
        Calendar calendar = Calendar.getInstance();
        int year_now = calendar.get(Calendar.YEAR);
        int month_now = calendar.get(Calendar.MONTH);
        int day_now = calendar.get(Calendar.DAY_OF_MONTH);
        month_now = month_now + 1;

        int date = Integer.parseInt(bar.getYear() + bar.getMonth() + bar.getDay());
        if (month_now >= 10 && day_now >= 10) {
            int date_now = Integer.parseInt(String.valueOf(year_now) + String.valueOf(month_now) + String.valueOf(day_now));
            if (date_now > date) {
                coupon_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coupon_databaseReference.removeValue();
                        list.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else if(month_now < 10 && day_now >= 10){
            int date_now = Integer.parseInt(String.valueOf(year_now) + "0" + String.valueOf(month_now) + String.valueOf(day_now));
            if (date_now > date) {
                coupon_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coupon_databaseReference.removeValue();
                        list.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else if (month_now >= 10 && day_now < 10){
            int date_now = Integer.parseInt(String.valueOf(year_now) + String.valueOf(month_now) + "0" + String.valueOf(day_now));
            if (date_now > date) {
                coupon_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coupon_databaseReference.removeValue();
                        list.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }else {
            int date_now = Integer.parseInt(String.valueOf(year_now) + "0" + String.valueOf(month_now) + "0" + String.valueOf(day_now));
            if (date_now > date) {
                coupon_databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coupon_databaseReference.removeValue();
                        list.clear();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView coupon_title, coupon_discount, coupon_serial_number, coupon_low_consumption, coupon_effective_date, coupon_from;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            coupon_title = itemView.findViewById(R.id.coupon_title);
            coupon_discount = itemView.findViewById(R.id.coupon_discount);
            coupon_serial_number = itemView.findViewById(R.id.coupon_serial_number);
            coupon_low_consumption = itemView.findViewById(R.id.coupon_low_consumption);
            coupon_effective_date = itemView.findViewById(R.id.coupon_effective_date);
            coupon_from = itemView.findViewById(R.id.coupon_from);

        }

    }
}
