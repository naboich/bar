package com.example.bar.diy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DIYdescfragment extends Fragment {

    String image,name,material,practice,suggest,from;
    long id;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DIYdescfragment(String image, String name, String material, String practice, String suggest, String from,long id) {
        this.image=image;
        this.name=name;
        this.material=material;
        this.practice=practice;
        this.suggest=suggest;
        this.from=from;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_d_i_ydescfragment, container, false);

        ImageView diy_imagegholder=view.findViewById(R.id.diy_imagegholder);
        TextView diy_nameholder=view.findViewById(R.id.diy_nameholder);
        TextView materialholder=view.findViewById(R.id.materialholder);
        TextView practiceholder=view.findViewById(R.id.practiceholder);
        TextView suggestlholder=view.findViewById(R.id.suggestlholder);
        TextView fromholder=view.findViewById(R.id.fromholder);

        Glide.with(getContext()).load(image).into(diy_imagegholder);
        diy_nameholder.setText(name);
        materialholder.setText(material);
        practiceholder.setText(practice);
        suggestlholder.setText(suggest);
        fromholder.setText(from);
        fromholder.setAutoLinkMask(Linkify.WEB_URLS);
        fromholder.setMovementMethod(LinkMovementMethod.getInstance());

        //fav
        ImageView fav_border = view.findViewById(R.id.fav_border);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("favorite_diy").child(String.valueOf(id));
        fav_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(view.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean fav_checker = snapshot.hasChild("fav_btn");
                            if (fav_checker == false) {
                                databaseReference.child("fav_btn").setValue(true);
                                databaseReference.child("image").setValue(image);
                                databaseReference.child("name").setValue(name);
                                databaseReference.child("material").setValue(material);
                                databaseReference.child("practice").setValue(practice);
                                databaseReference.child("suggest").setValue(suggest);
                                databaseReference.child("from").setValue(from);
                                databaseReference.child("id").setValue(id);
                                fav_border.setImageResource(R.drawable.favorite);
                            } else {
                                databaseReference.removeValue();
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

        return view;
    }
    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new DIYFragment()).addToBackStack(null).commit();

    }
}