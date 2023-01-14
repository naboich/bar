package com.example.bar.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.R;
import com.example.bar.home.HomeAdapter;
import com.example.bar.homemodel;
import com.example.bar.picturemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookingReviewAdapter extends RecyclerView.Adapter<BookingReviewAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public BookingReviewAdapter(android.content.Context context, ArrayList<homemodel> list) {
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
//        holder.picture_name.setText(list.get(position).getImagename());
        holder.picture_img.setImageURI(list.get(position).getImage_review());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView picture_name;
        ImageView picture_img;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
//            picture_name = itemView.findViewById(R.id.picture_name);
            picture_img = itemView.findViewById(R.id.picture_img);
        }
    }
}
