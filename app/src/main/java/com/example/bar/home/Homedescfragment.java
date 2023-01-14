package com.example.bar.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.booking.BookingdescFragment;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Homedescfragment extends Fragment {

    HomedescRecommendAdapter myAdapter;
    RecyclerView recommend_rv;
    ArrayList<homemodel> list,list_review;
    ImageView fav_border, booking_border;

    HomedescReviewAdapter homedescReviewAdapter;
    RecyclerView review_rv;

    String name, phone, place, image, style, facility, business, consumption, food, sign, activity, km, review,menu1,menu2;
    int id, count, rating;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Homedescfragment(String review, int rating, int count, int id, String km, String name, String place, String business, String phone, String style, String facility, String food, String sign, String activity, String image, String consumption,String menu1,String menu2) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.place = place;
        this.style = style;
        this.facility = facility;
        this.business = business;
        this.consumption = consumption;
        this.food = food;
        this.sign = sign;
        this.activity = activity;
        this.km = km;
        this.id = id;
        this.count = count;
        this.rating = rating;
        this.review = review;
        this.menu1 = menu1;
        this.menu2 = menu2;
    }

    public Homedescfragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homedescfragment, container, false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("home");
        DatabaseReference reference_review = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(id)).child("files");
        review_rv = view.findViewById(R.id.review_rv);
        review_rv.setHasFixedSize(true);
        review_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        list_review = new ArrayList<>();
        homedescReviewAdapter = new HomedescReviewAdapter(getContext(), list_review);
//        recommend_rv.setLayoutManager(layoutManager);
        review_rv.setAdapter(homedescReviewAdapter);
        reference_review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    homemodel review = dataSnapshot.getValue(homemodel.class);
                    list_review.add(review);
                }
                homedescReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //========================================================================
//        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        //recyclerview
        recommend_rv = view.findViewById(R.id.recommend_rv);
        recommend_rv.setHasFixedSize(true);
        recommend_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        myAdapter = new HomedescRecommendAdapter(getContext(), list);
//        recommend_rv.setLayoutManager(layoutManager);
        recommend_rv.setAdapter(myAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    Random random = new Random();
                    int index = random.nextInt((int) snapshot.getChildrenCount());
                    int index_2 = random.nextInt((int) snapshot.getChildrenCount());
                    int index_3 = random.nextInt((int) snapshot.getChildrenCount());
                    int count = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (index != index_2 && index != index_3 && index_2 != index_3) {
                            if (count == index) {
                                homemodel bar = dataSnapshot.getValue(homemodel.class);
                                list.add(bar);
                            }
                            if (count == index_2) {
                                homemodel bar = dataSnapshot.getValue(homemodel.class);
                                list.add(bar);
                            }
                            if (count == index_3) {
                                homemodel bar = dataSnapshot.getValue(homemodel.class);
                                list.add(bar);
                            }
                            count++;
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//=================================================================================================
        ImageView imageholder = view.findViewById(R.id.imagegholder);
        ImageView menu1holder = view.findViewById(R.id.menu1holder);
        ImageView menu2holder = view.findViewById(R.id.menu2holder);
        TextView nameholder = view.findViewById(R.id.nameholder);
        TextView phoneholder = view.findViewById(R.id.phoneholder);
        TextView placeholder = view.findViewById(R.id.placeholder);
        TextView styleholder = view.findViewById(R.id.styleholder);
        TextView facilityholder = view.findViewById(R.id.facilityholder);
        TextView businessholder = view.findViewById(R.id.businessholder);
        TextView consumptionholder = view.findViewById(R.id.consumptionholder);
        TextView foodholder = view.findViewById(R.id.foodholder);
        TextView signholder = view.findViewById(R.id.signholder);
        TextView activityholder = view.findViewById(R.id.activityholder);
        TextView kmholder = view.findViewById(R.id.kmholder);
        Linkify.addLinks(placeholder, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);

        nameholder.setText(name);
        phoneholder.setText(phone);
        placeholder.setText(place);
        Glide.with(getContext()).load(image).into(imageholder);
        Glide.with(getActivity()).load(menu1).into(menu1holder);
        Glide.with(getContext()).load(menu2).into(menu2holder);
        styleholder.setText(style);
        facilityholder.setText(facility);
        businessholder.setText(business);
        consumptionholder.setText(consumption);
        foodholder.setText(food);
        signholder.setText(sign);
        activityholder.setText(activity);
        kmholder.setText(km);

        //
        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(placeholder.getContext());
                builder.setTitle("是否前往google map?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://maps.google.com/maps?q=" + name;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                                    Toast.makeText(view.getContext(), "已取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        //fav
        fav_border = view.findViewById(R.id.fav_border);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("favorite_home").child(String.valueOf(id));
        fav_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(view.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(view.getContext(), snapshot.getKey(), Toast.LENGTH_SHORT).show();
                            boolean fav_checker = snapshot.hasChild("fav_btn");
                            if (fav_checker == false) {
                                reference.child("fav_btn").setValue(true);
                                reference.child("id").setValue(id);
                                reference.child("km").setValue(km);
                                reference.child("image").setValue(image);
                                reference.child("name").setValue(name);
                                reference.child("place").setValue(place);
                                reference.child("business").setValue(business);
                                reference.child("style").setValue(style);
                                reference.child("facility").setValue(facility);
                                reference.child("food").setValue(food);
                                reference.child("sign").setValue(sign);
                                reference.child("activity").setValue(activity);
                                reference.child("consumption").setValue(consumption);
                                reference.child("phone").setValue(phone);
                                reference.child("menu1").setValue(menu1);
                                reference.child("menu2").setValue(menu2);
                                fav_border.setImageResource(R.drawable.favorite);
                            } else if (fav_checker == true) {
                                reference.removeValue();
                                fav_border.setImageResource(R.drawable.favorite_border);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean fav_check = snapshot.hasChild("fav_btn");
                if (fav_check == true) {
                    fav_border.setImageResource(R.drawable.favorite);
                }
                if (fav_check == false) {
                    fav_border.setImageResource(R.drawable.favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//============================================================================================================
        //booking
        booking_border = view.findViewById(R.id.booking_border);
        DatabaseReference booking_reference = FirebaseDatabase.getInstance().getReference("home").child(String.valueOf(id));
        booking_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(view.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    booking_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            homemodel bar = snapshot.getValue(homemodel.class);
                            AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new BookingdescFragment(bar.getYear(),bar.getMonth(),bar.getDay(),bar.getHour(),bar.getMin(),bar.getDate(), bar.getTime(), bar.getPeople(), bar.getId(), bar.getKm(), bar.getName()
                                    , bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity()
                                    , bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        //
        menu1holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View mview = getLayoutInflater().inflate(R.layout.menu_item, null);
                TouchImageView menu_img = mview.findViewById(R.id.menu_img);
                Glide.with(getActivity()).load(menu1).into(menu_img);
                builder.setView(mview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        menu2holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View mview = getLayoutInflater().inflate(R.layout.menu_item, null);
                TouchImageView menu_img = mview.findViewById(R.id.menu_img);
                Glide.with(getActivity()).load(menu2).into(menu_img);
                builder.setView(mview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).addToBackStack(null).commit();

    }
}