package com.example.bar.home;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.favorite.FavoriteFragment;
import com.example.bar.homemodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    RecyclerView HomeFragment_rv;
    private MenuItem menuItem;
    private SearchView searchView;
    Toolbar toolbar;
    HomeAdapter myAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("home");

    ArrayList<homemodel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);
        toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("搜尋");
        HomeFragment_rv = root.findViewById(R.id.HomeFragment_rv);
        HomeFragment_rv.setHasFixedSize(true);
        HomeFragment_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        TabLayout tabLayout = root.findViewById(R.id.Fragment_TabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        list = new ArrayList<>();
        myAdapter = new HomeAdapter(getContext(), list);
        HomeFragment_rv.setAdapter(myAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    homemodel bar = dataSnapshot.getValue(homemodel.class);
//                    Collections.shuffle(list);
                    list.add(bar);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //
        ImageButton home_filter = root.findViewById(R.id.home_filter);
        home_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View mview = getLayoutInflater().inflate(R.layout.home_filter, null);

                RadioGroup distance_dialog = mview.findViewById(R.id.distance_dialog);
                RadioButton km_1 = mview.findViewById(R.id.km_1);
                RadioButton km_1_5 = mview.findViewById(R.id.km_1_5);
                RadioButton km_2 = mview.findViewById(R.id.km_2);
                CheckBox dart_filter = mview.findViewById(R.id.dart_filter);
                CheckBox board_game_filter = mview.findViewById(R.id.board_game_filter);
                CheckBox singing_machine_filter = mview.findViewById(R.id.singing_machine_filter);
                CheckBox pool_filter = mview.findViewById(R.id.pool_filter);
                CheckBox hand_football_filter = mview.findViewById(R.id.hand_football_filter);
                CheckBox musical_instrument_filter = mview.findViewById(R.id.musical_instrument_filter);
                CheckBox performance_filter = mview.findViewById(R.id.performance_filter);
                CheckBox game_console_filter = mview.findViewById(R.id.game_console_filter);
                CheckBox box_filter = mview.findViewById(R.id.box_filter);
                CheckBox projection_Screen_filter = mview.findViewById(R.id.projection_Screen_filter);
                CheckBox aquarium_filter = mview.findViewById(R.id.aquarium_filter);
                CheckBox television_filter = mview.findViewById(R.id.television_filter);
                CheckBox private_room_filter = mview.findViewById(R.id.private_room_filter);
                CheckBox American_style = mview.findViewById(R.id.American_style);
                CheckBox Japanese_style = mview.findViewById(R.id.Japanese_style);
                CheckBox Chinese_style = mview.findViewById(R.id.Chinese_style);
                CheckBox European_style = mview.findViewById(R.id.European_style);
                CheckBox generally_style = mview.findViewById(R.id.generally_style);
                CheckBox music_style = mview.findViewById(R.id.music_style);
                CheckBox party_style = mview.findViewById(R.id.party_style);
                CheckBox restaurant_style = mview.findViewById(R.id.restaurant_style);
                CheckBox industrial_style = mview.findViewById(R.id.industrial_style);
                CheckBox classical_style = mview.findViewById(R.id.classical_style);
                CheckBox urban_style = mview.findViewById(R.id.urban_style);
                CheckBox modern_style = mview.findViewById(R.id.modern_style);
                CheckBox gay_bar_style = mview.findViewById(R.id.gay_bar_style);

                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                show(view);
                            }

                            public void show(View view) {
                                String type = "";
                                if (km_1.isChecked()) {
                                    type = "1公里內";
                                } else if (km_1_5.isChecked()) {
                                    type = "1公里-1.5公里";
                                } else if (km_2.isChecked()) {
                                    type = "1.5公里-2公里";
                                }
                                //
                                String facility_dart = "";
                                if (dart_filter.isChecked()) {
                                    facility_dart = "飛鏢機";
                                }
                                String facility_board_game = "";
                                if (board_game_filter.isChecked()) {
                                    facility_board_game = "桌遊";
                                }
                                String facility_singing_machine = "";
                                if (singing_machine_filter.isChecked()) {
                                    facility_singing_machine = "唱歌機";
                                }
                                String facility_pool = "";
                                if (pool_filter.isChecked()) {
                                    facility_pool = "撞球";
                                }
                                String facility_hand_football = "";
                                if (hand_football_filter.isChecked()) {
                                    facility_hand_football = "手足球";
                                }
                                String facility_musical_instrument = "";
                                if (musical_instrument_filter.isChecked()) {
                                    facility_musical_instrument = "樂器";
                                }
                                String facility_performance = "";
                                if (performance_filter.isChecked()) {
                                    facility_performance = "表演";
                                }
                                String facility_game_console = "";
                                if (game_console_filter.isChecked()) {
                                    facility_game_console = "遊戲機";
                                }
                                String facility_box = "";
                                if (box_filter.isChecked()) {
                                    facility_box = "包廂";
                                }
                                String facility_Screen_filter = "";
                                if (projection_Screen_filter.isChecked()) {
                                    facility_Screen_filter = "投影幕";
                                }
                                String facility_aquarium = "";
                                if (aquarium_filter.isChecked()) {
                                    facility_aquarium = "水族箱";
                                }
                                String facility_television = "";
                                if (television_filter.isChecked()) {
                                    facility_television = "電視";
                                }
                                String facility_private_room = "";
                                if (private_room_filter.isChecked()) {
                                    facility_private_room = "包場";
                                }
                                //
                                String facility_American_style = "";
                                if (American_style.isChecked()) {
                                    facility_American_style = "美式";
                                }
                                String facility_Japanese_style = "";
                                if (Japanese_style.isChecked()) {
                                    facility_Japanese_style = "日式";
                                }
                                String facility_Chinese_style = "";
                                if (Chinese_style.isChecked()) {
                                    facility_Chinese_style = "中式";
                                }
                                String facility_European_style = "";
                                if (European_style.isChecked()) {
                                    facility_European_style = "歐式";
                                }
                                String facility_generally_style = "";
                                if (generally_style.isChecked()) {
                                    facility_generally_style = "一般";
                                }
                                String facility_music_style = "";
                                if (music_style.isChecked()) {
                                    facility_music_style = "音樂";
                                }
                                String facility_party_style = "";
                                if (party_style.isChecked()) {
                                    facility_party_style = "派對";
                                }
                                String facility_restaurant_style = "";
                                if (restaurant_style.isChecked()) {
                                    facility_restaurant_style = "餐酒館";
                                }
                                String facility_industrial_style = "";
                                if (industrial_style.isChecked()) {
                                    facility_industrial_style = "工業";
                                }
                                String facility_classical_style = "";
                                if (classical_style.isChecked()) {
                                    facility_classical_style = "復古";
                                }
                                String facility_urban_style = "";
                                if (urban_style.isChecked()) {
                                    facility_urban_style = "都市";
                                }
                                String facility_modern_style = "";
                                if (modern_style.isChecked()) {
                                    facility_modern_style = "現代";
                                }
                                String facility_gay_bar_style = "";
                                if (gay_bar_style.isChecked()) {
                                    facility_gay_bar_style = "GAY吧";
                                }
//                                Toast.makeText(view.getContext(), type, Toast.LENGTH_SHORT).show();
                                myfilter(type, facility_dart, facility_board_game, facility_singing_machine, facility_pool, facility_hand_football, facility_musical_instrument, facility_performance, facility_game_console
                                        , facility_box, facility_Screen_filter, facility_aquarium, facility_television, facility_private_room, facility_American_style, facility_Japanese_style, facility_Chinese_style, facility_European_style
                                        , facility_generally_style, facility_music_style, facility_party_style, facility_restaurant_style, facility_industrial_style, facility_classical_style, facility_urban_style, facility_modern_style, facility_gay_bar_style);


                            }
                        })
                        .setNegativeButton("取消", null);
//                        .create().show();
                builder.setView(mview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return root;
    }

    private void myfilter(String type, String facility_dart, String facility_board_game, String facility_singing_machine, String facility_pool, String facility_hand_football, String facility_musical_instrument
            , String facility_performance, String facility_game_console, String facility_box, String facility_Screen_filter, String facility_aquarium, String facility_television, String facility_private_room
            , String facility_American_style, String facility_Japanese_style, String facility_Chinese_style, String facility_European_style, String facility_generally_style, String facility_music_style, String facility_party_style
            , String facility_restaurant_style, String facility_industrial_style, String facility_classical_style, String facility_urban_style, String facility_modern_style, String facility_gay_bar_style) {
        List<homemodel> filteredList = new ArrayList<>();
        for (homemodel item : list) {
            if (item.getKm().toLowerCase().contains(type.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_dart.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_board_game.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_singing_machine.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_pool.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_hand_football.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_musical_instrument.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_performance.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_game_console.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_box.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_Screen_filter.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_aquarium.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_television.toLowerCase())
                    && item.getFacility().toLowerCase().contains(facility_private_room.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_American_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_Japanese_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_Chinese_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_European_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_generally_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_music_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_party_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_restaurant_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_industrial_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_classical_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_urban_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_modern_style.toLowerCase())
                    && item.getStyle().toLowerCase().contains(facility_gay_bar_style.toLowerCase())) {
                filteredList.add(item);
            }
        }
        myAdapter.filterList(filteredList);
    }

    //searchview
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        menuItem = menu.findItem(R.id.searchId);
//        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView = (SearchView) menuItem.getActionView();
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

    //
    private void mysearch(String query) {
        List<homemodel> filteredList = new ArrayList<>();
        for (homemodel item : list) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        myAdapter.filterList(filteredList);
    }

}
