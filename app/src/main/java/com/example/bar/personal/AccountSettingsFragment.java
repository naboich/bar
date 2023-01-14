package com.example.bar.personal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_settings, container, false);

        EditText name_et = root.findViewById(R.id.name_et);
        CircleImageView circleImageView = root.findViewById(R.id.circleImageView);
        TextView fullnameholder = root.findViewById(R.id.fullnameholder);
        EditText email_et = root.findViewById(R.id.email_et);
        EditText gender_et = root.findViewById(R.id.gender_et);
        EditText phone_et = root.findViewById(R.id.phone_et);
        EditText birthday_et = root.findViewById(R.id.birthday_et);


        //edit
        TextView edit_tv = root.findViewById(R.id.edit_tv);
        edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new AccountSettingsEditFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //personal file
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                User muser = snapshot.getValue(User.class);
                if (muser != null && muser.image != null) {
                    name_et.setText(muser.fullname);
                    fullnameholder.setText(muser.fullname);
                    email_et.setText(muser.email);
                    gender_et.setText(muser.getGender());
                    phone_et.setText(muser.getPhone());
                    birthday_et.setText(muser.getBirthday());
                    Glide.with(getActivity()).load(muser.image).into(circleImageView);
                } else if (muser != null && muser.email != null && muser.image == null) {
                    name_et.setText(muser.fullname);
                    fullnameholder.setText(muser.fullname);
                    email_et.setText(muser.email);
                    gender_et.setText(muser.getGender());
                    phone_et.setText(muser.getPhone());
                    birthday_et.setText(muser.getBirthday());
                    Glide.with(getActivity()).load(R.drawable.default_icon).into(circleImageView);
                } else if (user.getEmail() == null) {
                    name_et.setText("訪客");
                    fullnameholder.setText("訪客");
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
                                fullnameholder.setText(fullName);
                                Picasso.get().load(url).into(circleImageView);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User muser = snapshot.getValue(User.class);
                                        gender_et.setText(muser.getGender());
                                        phone_et.setText(muser.getPhone());
                                        birthday_et.setText(muser.getBirthday());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
            fullnameholder.setText(userName);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User muser = snapshot.getValue(User.class);
                    gender_et.setText(muser.getGender());
                    phone_et.setText(muser.getPhone());
                    birthday_et.setText(muser.getBirthday());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Glide.with(getActivity()).load(userPhotoUrl).into(circleImageView);

        }

        return root;
    }
}