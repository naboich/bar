package com.example.bar.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.User;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomedescRecommendAdapter extends RecyclerView.Adapter<HomedescRecommendAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;

    public HomedescRecommendAdapter(android.content.Context context, ArrayList<homemodel> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomedescRecommendAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomedescRecommendAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        homemodel bar = list.get(position);

        holder.nametext.setText(bar.getName());
        holder.phonetext.setText(bar.getPhone());
        holder.placetext.setText(bar.getPlace());

        Glide.with(holder.img1.getContext()).load(bar.getImage()).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Homedescfragment(bar.getReview(),bar.getRating(),bar.getCount(),bar.getId(), bar.getKm(),bar.getName(), bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity(), bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder {

        ImageView img1;
        TextView nametext,phonetext,placetext;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            nametext = itemView.findViewById(R.id.nametext);
            phonetext = itemView.findViewById(R.id.phonetext);
            placetext = itemView.findViewById(R.id.placetext);


        }
    }
}