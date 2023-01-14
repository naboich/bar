package com.example.bar.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomedescReviewPictureAdapter extends RecyclerView.Adapter<HomedescReviewPictureAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public HomedescReviewPictureAdapter(Context context, ArrayList<homemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        homemodel bar = list.get(position);
        Glide.with(holder.picture_img.getContext()).load(bar.getImage_review()).into(holder.picture_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        ImageView picture_img;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            picture_img = itemView.findViewById(R.id.picture_img);
        }
    }
}
