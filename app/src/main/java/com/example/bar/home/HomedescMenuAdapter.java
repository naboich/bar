package com.example.bar.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bar.R;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomedescMenuAdapter extends RecyclerView.Adapter<HomedescMenuAdapter.myviewholder> {

    Context context;
    ArrayList<homemodel> list_menu;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public HomedescMenuAdapter(Context context, ArrayList<homemodel> list) {
        this.context = context;
        this.list_menu = list_menu;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        homemodel bar = list_menu.get(position);
        Glide.with(holder.menu1.getContext()).load(bar.getMenu1()).into(holder.menu1);
//        Glide.with(holder.menu2.getContext()).load(bar.getMenu2()).into(holder.menu2);
    }

    @Override
    public int getItemCount() {
        return list_menu.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        ImageView menu1,menu2;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
//            menu2 = itemView.findViewById(R.id.menu1);
//            menu2 = itemView.findViewById(R.id.menu2);
        }
    }
}
