package com.example.bar.home;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public HomeAdapter(android.content.Context context, ArrayList<homemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diy_item, parent, false);
        return new HomeAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.myviewholder holder, int position) {

        homemodel bar = list.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("favorite_home").child(String.valueOf(bar.getId()));

        holder.hometv.setText(bar.getName());

        Glide.with(holder.diyimg.getContext()).load(bar.getImage()).into(holder.diyimg);

        holder.diyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Homedescfragment(bar.getReview(), bar.getRating(), bar.getCount(), bar.getId(), bar.getKm(), bar.getName(), bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity(), bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();

            }
        });

        holder.home_favorite_Btn.setOnClickListener(new View.OnClickListener() {
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
                                databaseReference.child("menu1").setValue(bar.getMenu1());
                                databaseReference.child("menu2").setValue(bar.getMenu2());
                                holder.home_favorite_Btn.setImageResource(R.drawable.favorite);
                            } else if (fav_checker == true) {
                                databaseReference.removeValue();
                                holder.home_favorite_Btn.setImageResource(R.drawable.favorite_border);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean fav_check = snapshot.hasChild("fav_btn");
                if (fav_check == true) {
                    holder.home_favorite_Btn.setImageResource(R.drawable.favorite);
                }
                if (fav_check == false) {
                    holder.home_favorite_Btn.setImageResource(R.drawable.favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void filterList(List<homemodel> filteredList) {
        list = (ArrayList<homemodel>) filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder {

        ImageView diyimg;
        TextView hometv;
        ImageButton home_favorite_Btn;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            diyimg = itemView.findViewById(R.id.diyimg);
            hometv = itemView.findViewById(R.id.diytv);
            home_favorite_Btn = itemView.findViewById(R.id.favorite_Btn);


        }
    }

}
