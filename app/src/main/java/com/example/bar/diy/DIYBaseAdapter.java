package com.example.bar.diy;

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
import com.example.bar.diymodel;
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

public class DIYBaseAdapter extends RecyclerView.Adapter<DIYBaseAdapter.myviewholder> {

    Context context;
    ArrayList<diymodel> list;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DIYBaseAdapter(android.content.Context context, ArrayList<diymodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DIYBaseAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diy_item, parent, false);
        return new DIYBaseAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DIYBaseAdapter.myviewholder holder, int position) {
        diymodel diymodel = list.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("favorite_diy").child(String.valueOf(diymodel.getId()));
        holder.diytvbase.setText(diymodel.getName());
        Glide.with(holder.diyimg.getContext()).load(diymodel.getImage()).into(holder.diyimg);
        holder.diyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new DIYdescfragment(diymodel.getImage(), diymodel.getName(), diymodel.getMaterial(), diymodel.getPractice(), diymodel.getSuggest(), diymodel.getFrom(),diymodel.getId())).addToBackStack(null).commit();
            }
        });

        holder.diy_favorite_Btn.setOnClickListener(new View.OnClickListener() {
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
                                holder.diy_favorite_Btn.setImageResource(R.drawable.favorite);
                                databaseReference.child("fav_btn").setValue(true);
                                databaseReference.child("image").setValue(diymodel.getImage());
                                databaseReference.child("name").setValue(diymodel.getName());
                                databaseReference.child("material").setValue(diymodel.getMaterial());
                                databaseReference.child("practice").setValue(diymodel.getPractice());
                                databaseReference.child("suggest").setValue(diymodel.getSuggest());
                                databaseReference.child("from").setValue(diymodel.getFrom());
                            } else {
                                databaseReference.removeValue();
                                holder.diy_favorite_Btn.setImageResource(R.drawable.favorite_border);
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
                    holder.diy_favorite_Btn.setImageResource(R.drawable.favorite);
                }
                if (fav_check == false) {
                    holder.diy_favorite_Btn.setImageResource(R.drawable.favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void filterList(List<diymodel> filteredList) {
        list = (ArrayList<diymodel>) filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView diyimg;
        TextView diytvbase;
        ImageButton diy_favorite_Btn;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            diyimg = itemView.findViewById(R.id.diyimg);
            diytvbase = itemView.findViewById(R.id.diytv);
            diy_favorite_Btn = itemView.findViewById(R.id.favorite_Btn);
        }
    }
}
