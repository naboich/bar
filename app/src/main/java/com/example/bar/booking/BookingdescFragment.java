package com.example.bar.booking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.home.HomeFragment;
import com.example.bar.homemodel;
import com.example.bar.personal.AccountSettingsEditFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingdescFragment extends Fragment {

    String name, phone, place, image, style, facility, business, consumption, food, sign, activity, km;
    int id;
    String date, time, people;
    String hour, min, year, month, day;
    String menu1,menu2;

    public BookingdescFragment(String hour, String min, String year, String month, String day, String date, String time, String people, int id, String km, String name, String place, String business, String phone, String style, String facility, String food, String sign, String activity, String image, String consumption,String menu1, String menu2) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.place = place;
        this.style = style;
        this.facility = facility;
        this.business = business;
        this.consumption = consumption;
        this.food = food;
        this.sign = sign;
        this.activity = activity;
        this.km = km;
        this.id = id;
        this.date = date;
        this.time = time;
        this.people = people;
        this.hour = hour;
        this.min = min;
        this.year = year;
        this.month = month;
        this.day = day;
        this.menu1 = menu1;
        this.menu2 = menu2;
    }

    EditText date_et;
    EditText time_et;
    Spinner people_sp;
    Button booking_confirm;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookingdesc, container, false);

        ImageView imageholder = view.findViewById(R.id.imagegholder);
        TextView nameholder = view.findViewById(R.id.nameholder);

        nameholder.setText(name);
        Glide.with(getContext()).load(image).into(imageholder);

        date_et = view.findViewById(R.id.date_et);
        date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year_now = calendar.get(Calendar.YEAR);
                int month_now = calendar.get(Calendar.MONTH);
                int day_now = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , dateSetListener
                        , year_now, month_now, day_now);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year_now, int month_now, int day_now) {
                month_now = month_now + 1;
//                Toast.makeText(getActivity(), year + "/" + month + "/" + day, Toast.LENGTH_SHORT).show();
                if (month_now >= 10 && day_now >= 10) {
                    String date1 = String.valueOf(year_now) + "/" + String.valueOf(month_now) + "/" + String.valueOf(day_now);
                    String date2 = String.valueOf(year_now) + String.valueOf(month_now) + String.valueOf(day_now);
                    date_et.setText(date1);
                    date = date2;
                    year = String.valueOf(year_now);
                    month = String.valueOf(month_now);
                    day = String.valueOf(day_now);
                } else if (month_now < 10 && day_now >= 10) {
                    String date1 = String.valueOf(year_now) + "/0" + String.valueOf(month_now) + "/" + String.valueOf(day_now);
                    String date2 = String.valueOf(year_now) + "0" + String.valueOf(month_now) + String.valueOf(day_now);
                    date_et.setText(date1);
                    date = date2;
                    year = String.valueOf(year_now);
                    month = "0" + String.valueOf(month_now);
                    day = String.valueOf(day_now);
                } else if (month_now >= 10 && day_now < 10) {
                    String date1 = String.valueOf(year_now) + "/" + String.valueOf(month_now) + "/0" + String.valueOf(day_now);
                    String date2 = String.valueOf(year_now) + String.valueOf(month_now) + "0" + String.valueOf(day_now);
                    date_et.setText(date1);
                    date = date2;
                    year = String.valueOf(year_now);
                    month = String.valueOf(month_now);
                    day = "0" + String.valueOf(day_now);
                } else if (month_now < 10 && day_now < 10) {
                    String date1 = String.valueOf(year_now) + "/0" + String.valueOf(month_now) + "/0" + String.valueOf(day_now);
                    String date2 = String.valueOf(year_now) + "0" + String.valueOf(month_now) + "0" + String.valueOf(day_now);
                    date_et.setText(date1);
                    date = date2;
                    year = String.valueOf(year_now);
                    month = "0" + String.valueOf(month_now);
                    day = "0" + String.valueOf(day_now);
                }
            }
        };

        time_et = view.findViewById(R.id.time_et);

        time_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
                int min_now = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getActivity()
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , timeSetListener
                        , hour_now, min_now, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour_now, int min_now) {
                if (min_now < 10) {
                    String time1 = String.valueOf(hour_now) + ":0" + String.valueOf(min_now);
                    String time2 = String.valueOf(hour_now) + "0" + String.valueOf(min_now);
                    time_et.setText(time1);
                    time = time2;
                    hour = String.valueOf(hour_now);
                    min = "0" + String.valueOf(min_now);
                } else {
                    String time1 = String.valueOf(hour_now) + ":" + String.valueOf(min_now);
                    String time2 = String.valueOf(hour_now) + String.valueOf(min_now);
                    time_et.setText(time1);
                    time = time2;
                    hour = String.valueOf(hour_now);
                    min = String.valueOf(min_now);
                }
            }
        };

        people_sp = view.findViewById(R.id.people_sp);
//
        people_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] peoples = getResources().getStringArray(R.array.peoples);
                int index = people_sp.getSelectedItemPosition();
                people = peoples[index];
//                Toast.makeText(getContext(), people, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        booking_confirm = view.findViewById(R.id.booking_confirm);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("booking").child(String.valueOf(id));
        booking_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setTitle("訂位處理中...");
                pd.show();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (date == null && time == null) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "請選擇日期與時間", Toast.LENGTH_SHORT).show();
                        } else if (date == null) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "請選擇日期", Toast.LENGTH_SHORT).show();
                        } else if (time == null) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "請選擇時間", Toast.LENGTH_SHORT).show();
                        } else {
//                            Log.v("date", year + month + day);
//                            Log.v("time", hour + min);
                            pd.dismiss();
                            reference.child("id").setValue(id);
                            reference.child("km").setValue(km);
                            reference.child("image").setValue(image);
                            reference.child("name").setValue(name);
                            reference.child("place").setValue(place);
                            reference.child("business").setValue(business);
                            reference.child("style").setValue(style);
                            reference.child("facility").setValue(facility);
                            reference.child("food").setValue(food);
                            reference.child("sign").setValue(sign);
                            reference.child("activity").setValue(activity);
                            reference.child("consumption").setValue(consumption);
                            reference.child("phone").setValue(phone);
                            reference.child("date").setValue(date);
                            reference.child("time").setValue(time);
                            reference.child("year").setValue(year);
                            reference.child("month").setValue(month);
                            reference.child("day").setValue(day);
                            reference.child("hour").setValue(hour);
                            reference.child("menu1").setValue(menu1);
                            reference.child("menu2").setValue(menu2);
                            reference.child("min").setValue(min);
                            reference.child("people").setValue(people);

                            startActivity(new Intent(getActivity(), MainActivity.class));
                            Toast.makeText(getActivity(),"訂位成功",Toast.LENGTH_SHORT).show();
//                            getActivity().getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.frame, new BookingFragment(), null)
//                                    .addToBackStack(null)
//                                    .commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }

}