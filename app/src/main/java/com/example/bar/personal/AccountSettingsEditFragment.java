package com.example.bar.personal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsEditFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date, final_gender, phonenum, rename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account_settings_edit, container, false);

        EditText name_et = root.findViewById(R.id.name_et);
        CircleImageView circleImageView = root.findViewById(R.id.circleImageView);
        EditText email_et = root.findViewById(R.id.email_et);
        Spinner gender_sp = root.findViewById(R.id.gender_sp);
        EditText phone_et = root.findViewById(R.id.phone_et);
        EditText birthday_et = root.findViewById(R.id.birthday_et);

        //personal file
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                User muser = snapshot.getValue(User.class);
                if (muser != null && muser.image != null) {
                    name_et.setText(muser.fullname);
                    email_et.setText(muser.email);
                    gender_sp.getSelectedItemPosition();
                    phone_et.setText(muser.getPhone());
                    birthday_et.setText(muser.getBirthday());
                    Glide.with(getActivity()).load(muser.image).into(circleImageView);
                } else if (muser != null && muser.email != null && muser.image == null) {
                    name_et.setText(muser.fullname);
                    email_et.setText(muser.email);
                    gender_sp.getSelectedItemPosition();
                    phone_et.setText(muser.getPhone());
                    birthday_et.setText(muser.getBirthday());
                    Glide.with(getActivity()).load(R.drawable.default_icon).into(circleImageView);
                } else if (user.getEmail() == null) {
                    name_et.setText("訪客");
                    Glide.with(getActivity()).load(R.drawable.default_icon).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //facebook
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn;
        if (isLoggedIn = accessToken != null && !accessToken.isExpired()) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            try {
                                String fullName = object.getString("name");
                                String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                name_et.setText(fullName);
                                Picasso.get().load(url).into(circleImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }
        //google
        else if (signInAccount != null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userName = preferences.getString("username", "");
            String userPhotoUrl = preferences.getString("userPhoto", "");
            name_et.setText(userName);
            Glide.with(getActivity()).load(userPhotoUrl).into(circleImageView);

        }

        //rename
        rename = name_et.getText().toString();
        if (rename.length() > 16) {
            Toast.makeText(getActivity(), "暱稱不可大於16字元", Toast.LENGTH_SHORT).show();
        } else {
//                    Toast.makeText(getActivity(),rename,Toast.LENGTH_SHORT).show();
        }
        //gender
        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String gender;
                String[] gender_choose = getResources().getStringArray(R.array.gender);
                int choose = gender_sp.getSelectedItemPosition();
                gender = gender_choose[choose];
                final_gender = gender;
//                        Toast.makeText(getActivity(),gender,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //phone
        phonenum = phone_et.getText().toString();
        if (phonenum.length() != 10 && !phonenum.isEmpty()) {
            Toast.makeText(getActivity(), "手機格式錯誤", Toast.LENGTH_SHORT).show();
        } else {
//                    Toast.makeText(getActivity(),phonenum,Toast.LENGTH_SHORT).show();
        }
        //birthday
        birthday_et.setOnClickListener(new View.OnClickListener() {
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
                if (month_now >= 10 && day_now >= 10) {
                    date = String.valueOf(year_now) + "/" + String.valueOf(month_now) + "/" + String.valueOf(day_now);
                    birthday_et.setText(date);
                } else if (month_now < 10 && day_now >= 10) {
                    date = String.valueOf(year_now) + "/0" + String.valueOf(month_now) + "/" + String.valueOf(day_now);
                    birthday_et.setText(date);
                } else if (month_now >= 10 && day_now < 10) {
                    date = String.valueOf(year_now) + "/" + String.valueOf(month_now) + "/0" + String.valueOf(day_now);
                    birthday_et.setText(date);
                } else if (month_now < 10 && day_now < 10) {
                    date = String.valueOf(year_now) + "/0" + String.valueOf(month_now) + "/0" + String.valueOf(day_now);
                    birthday_et.setText(date);
                }
            }
        };

        //save
        Button save_btn = root.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("fullname").setValue(rename);
                        databaseReference.child("gender").setValue(final_gender);
                        databaseReference.child("phone").setValue(phonenum);
                        databaseReference.child("birthday").setValue(date);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
//                        Toast.makeText(getActivity(),muser.fullname+"/"+muser.getBirthday()+"/"+muser.getPhone()+"/"+muser.getGender(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return root;
    }
}