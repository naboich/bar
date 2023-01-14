package com.example.bar.booking;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.home.HomeAdapter;
import com.example.bar.home.Homedescfragment;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public BookingHistoryAdapter(android.content.Context context, ArrayList<homemodel> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_item, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        homemodel bar = list.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("favorite_home").child(String.valueOf(bar.getId()));

        holder.name_tv.setText(bar.getName());
        holder.date_tv.setText("預約日期 : " + bar.getYear()+"/"+bar.getMonth()+"/"+bar.getDay());
        holder.time_tv.setText("預約時間 : " + bar.getHour()+":"+bar.getMin());
        holder.people_tv.setText("人數 : " + bar.getPeople());

        Glide.with(holder.img.getContext()).load(bar.getImage()).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Homedescfragment(bar.getReview(),bar.getRating(),bar.getCount(),bar.getId(), bar.getKm(), bar.getName(), bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity(), bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();

            }
        });

        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean fav_checker = snapshot.hasChild("fav_btn");
                        if (fav_checker == false) {
                            databaseReference.child("fav_btn").setValue(true);
                            databaseReference.child("id").setValue(bar.getId());
                            databaseReference.child("km").setValue(bar.getKm());
                            databaseReference.child("image").setValue(bar.getImage());
                            databaseReference.child("name").setValue(bar.getName());
                            databaseReference.child("place").setValue(bar.getPlace());
                            databaseReference.child("business").setValue(bar.getBusiness());
                            databaseReference.child("style").setValue(bar.getStyle());
                            databaseReference.child("facility").setValue(bar.getFacility());
                            databaseReference.child("food").setValue(bar.getFood());
                            databaseReference.child("sign").setValue(bar.getSign());
                            databaseReference.child("activity").setValue(bar.getActivity());
                            databaseReference.child("consumption").setValue(bar.getConsumption());
                            databaseReference.child("phone").setValue(bar.getPhone());
                            holder.add_fav.setImageResource(R.drawable.favorite);
                        } else if (fav_checker == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.name_tv.getContext());
                            builder.setTitle("確定要移除嗎?");
                            builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.removeValue();
                                    holder.add_fav.setImageResource(R.drawable.favorite_border);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean fav_check = snapshot.hasChild("fav_btn");
                if (fav_check == true) {
                    holder.add_fav.setImageResource(R.drawable.favorite);
                }
                if (fav_check == false) {
                    holder.add_fav.setImageResource(R.drawable.favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.booking_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new BookingdescFragment(bar.getYear(),bar.getMonth(),bar.getDay(),bar.getHour(),bar.getMin(),bar.getDate(), bar.getTime(), bar.getPeople(), bar.getId(), bar.getKm(), bar.getName()
                        , bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity()
                        , bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();
            }
        });

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new BookingReviewFragment(bar.getKey(),bar.getRating(),bar.getUser_name(),bar.getUser_id(),bar.getUser_image(),bar.getDate(), bar.getTime(), bar.getPeople(), bar.getId(), bar.getKm(), bar.getName()
                        , bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity()
                        , bar.getImage(), bar.getConsumption())).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name_tv, date_tv, time_tv, people_tv;
        Button booking_again,review;
        ImageButton add_fav;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name_tv = itemView.findViewById(R.id.name_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
            people_tv = itemView.findViewById(R.id.people_tv);
            add_fav = itemView.findViewById(R.id.add_fav);
            booking_again = itemView.findViewById(R.id.booking_again);
            review = itemView.findViewById(R.id.review);
        }
    }
}
