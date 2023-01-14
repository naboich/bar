package com.example.bar.home;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.User;
import com.example.bar.booking.BookingReviewAdapter;
import com.example.bar.homemodel;
import com.example.bar.picturemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomedescReviewAdapter extends RecyclerView.Adapter<HomedescReviewAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list_review;
    ArrayList<homemodel> list_picture;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int count = 0;

    public HomedescReviewAdapter(android.content.Context context, ArrayList<homemodel> list_review) {
        this.context = context;
        this.list_review = list_review;
    }

    @NonNull
    @Override
    public HomedescReviewAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomedescReviewAdapter.myviewholder holder, int position) {

        homemodel bar = list_review.get(position);

        holder.nameholder.setText(bar.getUser_name());
        Glide.with(holder.circleImageView.getContext()).load(bar.getUser_image()).into(holder.circleImageView);
        holder.review_tv.setText(bar.getReview());
        holder.ratingBar.setRating(bar.getRating());
        holder.good_tv.setText(String.valueOf(bar.getCount()));
        Glide.with(holder.img0.getContext()).load(bar.getImage_review0()).into(holder.img0);
        Glide.with(holder.img1.getContext()).load(bar.getImage_review1()).into(holder.img1);
        Glide.with(holder.img2.getContext()).load(bar.getImage_review2()).into(holder.img2);
//            Toast.makeText(getContext(),bar.getImage_review()+0,Toast.LENGTH_SHORT).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(bar.getId())).child("files").child(bar.getKey());
        holder.good_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String key = reference.push().getKey();
//                String key2 = reference.push().getKey();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean user_checker = snapshot.hasChild(user.getUid());
                        if (user_checker == false) {
                            count = bar.getCount() + 1;
                            reference.child(user.getUid()).setValue(true);
//                            reference.child(user.getUid()).setValue(true);
                            reference.child("count").setValue(count);
                            holder.good_tv.setText(String.valueOf(count));
                            holder.good_img_btn.setImageResource(R.drawable.thumb_up_alt_black);
                        }
                        if (user_checker == true) {
                            count = bar.getCount() - 1;
                            reference.child(user.getUid()).removeValue();
                            reference.child("count").setValue(count);
//                            reference.child(user.getUid()).removeValue();
                            holder.good_tv.setText(String.valueOf(count));
                            holder.good_img_btn.setImageResource(R.drawable.thumb_up_alt);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean good_checker = snapshot.hasChild("good_btn");
                boolean user_checker = snapshot.hasChild(user.getUid());
                if (user_checker == true) {
                    holder.good_img_btn.setImageResource(R.drawable.thumb_up_alt_black);
                }
                if (user_checker == false) {
                    holder.good_img_btn.setImageResource(R.drawable.thumb_up_alt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_review.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView nameholder, review_tv, good_tv;
        RatingBar ratingBar;
        ImageButton good_img_btn;
        ImageView img0, img1, img2;
//        RecyclerView review_rv;
//        HomedescReviewPictureAdapter homedescReviewPictureAdapter;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            nameholder = itemView.findViewById(R.id.nameholder);
            review_tv = itemView.findViewById(R.id.review_tv);
            good_tv = itemView.findViewById(R.id.good_tv);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            good_img_btn = itemView.findViewById(R.id.good_img_btn);
            img0 = itemView.findViewById(R.id.img0);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
//            review_rv = itemView.findViewById(R.id.review_rv);
//
//
//
//            review_rv.findViewById(R.id.review_rv);
//            review_rv.setHasFixedSize(true);
//            review_rv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
//
//            list_picture = new ArrayList<>();
//            homedescReviewPictureAdapter = new HomedescReviewPictureAdapter(context.getApplicationContext(), list_picture);
//            review_rv.setAdapter(homedescReviewPictureAdapter);
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(33)).child("image_review").child("-NG2_BoEHer7zDgQrOn0");
////            Log.v("data",String.valueOf(bar.getId()));
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        homemodel bar = dataSnapshot.getValue(homemodel.class);
//                        list_picture.add(bar);
//                    }
//                    homedescReviewPictureAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
        }
    }
}
