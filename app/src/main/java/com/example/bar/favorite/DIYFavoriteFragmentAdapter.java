package com.example.bar.favorite;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.diy.DIYdescfragment;
import com.example.bar.diymodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DIYFavoriteFragmentAdapter extends FirebaseRecyclerAdapter<diymodel, DIYFavoriteFragmentAdapter.myviewholder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DIYFavoriteFragmentAdapter(@NonNull FirebaseRecyclerOptions<diymodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull diymodel diymodel) {

        DatabaseReference databaseReference = database.getReference("users").child(user.getUid()).child("favorite_diy").child(getRef(position).getKey());
        holder.diytv.setText(diymodel.getName());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.diytv.getContext());
                builder.setTitle("確定要移除嗎?");
                builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.removeValue();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(view.getContext(), "已取消", Toast.LENGTH_SHORT).show();
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

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diy_item, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        ImageView diyimg;
        TextView diytv;
        ImageButton diy_favorite_Btn;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            diyimg = itemView.findViewById(R.id.diyimg);
            diytv = itemView.findViewById(R.id.diytv);
            diy_favorite_Btn = itemView.findViewById(R.id.favorite_Btn);
        }
    }
}
