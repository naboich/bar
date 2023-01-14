package com.example.bar.personal;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bar.Login;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.User;
import com.example.bar.booking.BookingdescFragment;
import com.example.bar.coupon.CouponFragment;
import com.example.bar.home.Homedescfragment;
import com.example.bar.homemodel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {

    TextView fullnameTxt;
    CircleImageView circleImageView;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_personal, container, false);
        //
        circleImageView = root.findViewById(R.id.circleImageView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(root.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(circleImageView.getContext());
                    builder.setTitle("要更換大頭貼嗎?");
                    builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            choosePicture();
                        }
                    });
                    builder.show();
                }
            }
        });


        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        fullnameTxt = root.findViewById(R.id.fullnameholder);

        //personal file
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                User muser = snapshot.getValue(User.class);
                if (muser != null && muser.image != null) {
                    fullnameTxt.setText(muser.fullname);
                    Glide.with(getActivity()).load(muser.image).into(circleImageView);
                } else if (muser != null && muser.email != null && muser.image == null) {
                    fullnameTxt.setText(muser.fullname);
                    Glide.with(getActivity()).load(R.drawable.default_icon).into(circleImageView);
                } else if (user.getEmail() == null) {
                    fullnameTxt.setText("訪客");
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
                                fullnameTxt.setText(fullName);
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

            fullnameTxt.setText(userName);
            Glide.with(getActivity()).load(userPhotoUrl).into(circleImageView);

        }

        //change name
        fullnameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(root.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View mview = getLayoutInflater().inflate(R.layout.personal_rename, null);
                    builder
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText rename_et = mview.findViewById(R.id.rename_et);
                                    String rename = rename_et.getText().toString();
                                    if (rename.isEmpty()) {
                                        Toast.makeText(getActivity(), "暱稱不可為空", Toast.LENGTH_SHORT).show();
                                    } else if (rename.length() > 16) {
                                        Toast.makeText(getActivity(), "暱稱不可大於16字元", Toast.LENGTH_SHORT).show();
                                    } else {
                                        databaseReference.child("fullname").setValue(rename);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null);
//                            .create().show();
                    builder.setView(mview);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        //1D barcode
        ImageButton barcode_btn = root.findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(view.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View mview = getLayoutInflater().inflate(R.layout.personal_barcode, null);

                    ImageView close_img = mview.findViewById(R.id.close_img);
                    ImageView barcode_img = mview.findViewById(R.id.barcode_img);
                    TextView barcode_tv = mview.findViewById(R.id.barcode_tv);

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    barcode_img.post(new Runnable() {
                        @Override
                        public void run() {
                            int high = barcode_img.getHeight();
                            int width = barcode_img.getWidth();

                            try {
                                BitMatrix bitMatrix = multiFormatWriter.encode(user.getUid(), BarcodeFormat.CODE_128, width, high);
                                Bitmap bitmap = Bitmap.createBitmap(width, high, Bitmap.Config.RGB_565);
                                for (int i = 0; i < width; i++) {
                                    for (int j = 0; j < high; j++) {
                                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                                    }
                                }
                                barcode_img.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    barcode_tv.setText(user.getUid());

                    builder.setView(mview);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    close_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                }
            }
        });

        //logout
        Button logoutBtn = root.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(logoutBtn.getContext());
                builder.setTitle("確定要登出?");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setNegativeButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    }
                });
                builder.show();

                //
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (user.getEmail() == null) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //coupon
        Button personal_coupon = root.findViewById(R.id.personal_coupon);
        personal_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new CouponFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //common problem
        Button common_problem_btn = root.findViewById(R.id.common_problem_btn);
        common_problem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new CommonProblemFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //satisfaction
        Button satisfaction_btn = root.findViewById(R.id.satisfaction_btn);
        satisfaction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //url
            }
        });

        //about us
        Button about_us_btn = root.findViewById(R.id.about_us_btn);
        about_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new AboutUsFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //settings
        Button settings_btn = root.findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, new SettingsFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //problem report
        Button problem_report_btn = root.findViewById(R.id.problem_report_btn);
        problem_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //app teaching
        Button app_teaching_btn = root.findViewById(R.id.app_teaching_btn);
        app_teaching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://youtu.be/tj9p8nkipXw";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);            }
        });

        //add coupon code
        DatabaseReference coupon_databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("coupon");
        Button add_coupon_code = root.findViewById(R.id.add_coupon_code);
        add_coupon_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEmail() == null) {
                    Toast.makeText(root.getContext(), "請登入", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View mview = getLayoutInflater().inflate(R.layout.personal_add_coupon, null);
                    builder
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText add_coupon_et = mview.findViewById(R.id.coupon_code_et);
                                    String add_coupon = add_coupon_et.getText().toString();
                                    if (add_coupon.isEmpty()) {
                                        Toast.makeText(getActivity(), "請輸入優惠碼", Toast.LENGTH_SHORT).show();
                                    } else if (add_coupon.equals("greendoor2266")) {
                                        coupon_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean coupon_checker = snapshot.child("0").hasChild("coupon_check");
                                                if (coupon_checker == false) {
                                                    coupon_databaseReference.child("0").child("coupon_check").setValue(true).toString();
                                                    coupon_databaseReference.child("0").child("coupon_title").setValue("一杯調酒85折");
                                                    coupon_databaseReference.child("0").child("coupon_discount").setValue("15%");
                                                    coupon_databaseReference.child("0").child("coupon_serial_number").setValue(add_coupon);
                                                    coupon_databaseReference.child("0").child("coupon_low_consumption").setValue("$100");
                                                    coupon_databaseReference.child("0").child("year").setValue("2022");
                                                    coupon_databaseReference.child("0").child("month").setValue("11");
                                                    coupon_databaseReference.child("0").child("day").setValue("01");
                                                    coupon_databaseReference.child("0").child("coupon_from").setValue("Green Door 綠門餐酒館");
//                                                    coupon_databaseReference.child("0").child("id").setValue("0");
                                                    Toast.makeText(getActivity(), "成功加入優惠碼" + add_coupon, Toast.LENGTH_SHORT).show();
                                                } else if (coupon_checker == true) {
                                                    Toast.makeText(getActivity(), "優惠碼已領取過", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else if (add_coupon.equals("88bar")) {
                                        coupon_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean coupon_checker = snapshot.child("3").hasChild("coupon_check");
                                                if (coupon_checker == false) {
                                                    coupon_databaseReference.child("3").setValue("3");
                                                    coupon_databaseReference.child("3").child("coupon_check").setValue(true).toString();
                                                    coupon_databaseReference.child("3").child("coupon_title").setValue("800元無限暢飲");
                                                    coupon_databaseReference.child("3").child("coupon_serial_number").setValue(add_coupon);
                                                    coupon_databaseReference.child("3").child("coupon_low_consumption").setValue("無");
                                                    coupon_databaseReference.child("3").child("year").setValue("2022");
                                                    coupon_databaseReference.child("3").child("month").setValue("11");
                                                    coupon_databaseReference.child("3").child("day").setValue("31");
                                                    coupon_databaseReference.child("3").child("coupon_from").setValue("88 sports bar");
//                                                    coupon_databaseReference.child("3").child("id").setValue("3");
                                                    Toast.makeText(getActivity(), "成功加入優惠碼" + add_coupon, Toast.LENGTH_SHORT).show();
                                                } else if (coupon_checker == true) {
                                                    Toast.makeText(getActivity(), "優惠碼已領取過", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else if (add_coupon.equals("waybar")) {
                                        coupon_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean coupon_checker = snapshot.child("46").hasChild("coupon_check");
                                                if (coupon_checker == false) {
                                                    coupon_databaseReference.child("46").setValue("46");
                                                    coupon_databaseReference.child("46").child("coupon_check").setValue(true).toString();
                                                    coupon_databaseReference.child("46").child("coupon_title").setValue("免費調酒*1");
                                                    coupon_databaseReference.child("46").child("coupon_serial_number").setValue(add_coupon);
                                                    coupon_databaseReference.child("46").child("coupon_low_consumption").setValue("500");
                                                    coupon_databaseReference.child("46").child("year").setValue("2023");
                                                    coupon_databaseReference.child("46").child("month").setValue("3");
                                                    coupon_databaseReference.child("46").child("day").setValue("31");
                                                    coupon_databaseReference.child("46").child("coupon_from").setValue("WAY BAR");
//                                                    coupon_databaseReference.child("3").child("id").setValue("3");
                                                    Toast.makeText(getActivity(), "成功加入優惠碼" + add_coupon, Toast.LENGTH_SHORT).show();
                                                } else if (coupon_checker == true) {
                                                    Toast.makeText(getActivity(), "優惠碼已領取過", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "請輸入正確的優惠碼", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null);
//                            .create().show();
                    builder.setView(mview);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return root;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            circleImageView.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("上傳中...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("personal_picture").child(user.getUid()).child("images/");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "上傳成功", Snackbar.LENGTH_LONG).show();

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        databaseReference.child("image").setValue(uri.toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "上傳失敗", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }
}