package com.example.bar.favorite;

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
import com.example.bar.home.Homedescfragment;
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

public class HomeFavoriteFragmentAdapter extends FirebaseRecyclerAdapter<homemodel, HomeFavoriteFragmentAdapter.myviewholder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public HomeFavoriteFragmentAdapter(@NonNull FirebaseRecyclerOptions<homemodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull homemodel bar) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("home").child(String.valueOf(position));
        DatabaseReference databaseReference = database.getReference("users").child(user.getUid()).child("favorite_home").child(getRef(position).getKey());
        holder.hometv.setText(bar.getName());
        Glide.with(holder.diyimg.getContext()).load(bar.getImage()).into(holder.diyimg);
        holder.diyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Homedescfragment(bar.getReview(),bar.getRating(),bar.getCount(),bar.getId(), bar.getKm(),bar.getName(), bar.getPlace(), bar.getBusiness(), bar.getPhone(), bar.getStyle(), bar.getFacility(), bar.getFood(), bar.getSign(), bar.getActivity(), bar.getImage(), bar.getConsumption(),bar.getMenu1(), bar.getMenu2())).addToBackStack(null).commit();

            }
        });

        holder.home_favorite_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.hometv.getContext());
                builder.setTitle("???????????????????");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.removeValue();
                    }
                });

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "?????????", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diy_item, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

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