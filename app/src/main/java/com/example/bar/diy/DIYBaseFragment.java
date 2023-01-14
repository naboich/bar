package com.example.bar.diy;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.R;
import com.example.bar.diymodel;
import com.example.bar.home.HomeAdapter;
import com.example.bar.homemodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DIYBaseFragment extends Fragment {

    RecyclerView diybase_rv;
    DIYBaseAdapter diybaseAdapter;
    private MenuItem menuItem;
    private SearchView searchView;
    Toolbar toolbar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("diybase");
    ArrayList<diymodel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_d_i_y_base, container, false);

        setHasOptionsMenu(true);
        toolbar = root.findViewById(R.id.diy_base_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("搜尋");

        diybase_rv = (RecyclerView)root.findViewById(R.id.diybase_rv);
        diybase_rv.setHasFixedSize(true);
        diybase_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        diybaseAdapter = new DIYBaseAdapter(getContext(),list);
        diybase_rv.setAdapter(diybaseAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    diymodel diymodel = dataSnapshot.getValue(diymodel.class);
//                    Collections.shuffle(list);
                    list.add(diymodel);
                }
                diybaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        menuItem = menu.findItem(R.id.searchId);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconified(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mysearch(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void mysearch(String query) {
        List<diymodel> filteredList = new ArrayList<>();
        for (diymodel item : list){
            if (item.getName().toLowerCase().contains(query.toLowerCase())){
                filteredList.add(item);
            }
        }
        diybaseAdapter.filterList(filteredList);

    }
}