package com.example.bar.booking;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bar.MainActivity;
import com.example.bar.R;
import com.example.bar.User;
import com.example.bar.home.Homedescfragment;
import com.example.bar.homemodel;
import com.example.bar.picturemodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.HashMap;

public class BookingReviewFragment extends Fragment {

    private static final int IMAGE_CODE = 1, PICK_VIDEO = 1;
    String name, phone, place, image, style, facility, business, consumption, food, sign, activity, km, key;
    int id, rating;
    String date, time, people, user_name, user_image, user_id;
    int count = 0;
    ArrayList<homemodel> list;
    RecyclerView picture_rv;
    BookingReviewAdapter bookingReviewAdapter;
    int j = 0;

    public BookingReviewFragment(String key, int rating, String user_id, String user_name, String user_image, String date, String time, String people, int id, String km, String name, String place, String business, String phone, String style, String facility, String food, String sign, String activity, String image, String consumption) {
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
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
        this.rating = rating;
        this.key = key;
    }

    private StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(id)).child("files");//.child(user.getUid());
    String mkey = reference.push().getKey();

    Button picture_btn, video_btn, review_confirm;
    RatingBar ratingBar;
    EditText review_et;
    UploadTask uploadTask;
    Uri videouri;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_booking_review, container, false);

        picture_rv = root.findViewById(R.id.picture_rv);
        picture_rv.setHasFixedSize(true);
        picture_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        picture_rv.setLayoutManager(layoutManager);

        storageReference = FirebaseStorage.getInstance().getReference();

        //

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User muser = snapshot.getValue(User.class);
                user_name = muser.fullname;
                user_image = muser.image;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView imageholder = root.findViewById(R.id.imagegholder);
        TextView nameholder = root.findViewById(R.id.nameholder);

        nameholder.setText(name);
        Glide.with(getContext()).load(image).into(imageholder);

        ratingBar = root.findViewById(R.id.ratingBar);
        review_confirm = root.findViewById(R.id.review_confirm);
        review_et = root.findViewById(R.id.review_et);
        list = new ArrayList<>();

        review_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = (int) ratingBar.getRating();
                String review = review_et.getText().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(id)).child("files");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child(mkey).child("user_name").setValue(user_name);
                        reference.child(mkey).child("user_image").setValue(user_image);
//                        reference.child(key).child("user_id").setValue(user.getUid());
                        reference.child(mkey).child("rating").setValue(rating);
                        reference.child(mkey).child("review").setValue(review);
                        reference.child(mkey).child("id").setValue(id);
                        reference.child(mkey).child("km").setValue(km);
                        reference.child(mkey).child("image").setValue(image);
                        reference.child(mkey).child("name").setValue(name);
                        reference.child(mkey).child("place").setValue(place);
                        reference.child(mkey).child("business").setValue(business);
                        reference.child(mkey).child("style").setValue(style);
                        reference.child(mkey).child("facility").setValue(facility);
                        reference.child(mkey).child("food").setValue(food);
                        reference.child(mkey).child("sign").setValue(sign);
                        reference.child(mkey).child("activity").setValue(activity);
                        reference.child(mkey).child("consumption").setValue(consumption);
                        reference.child(mkey).child("phone").setValue(phone);
                        reference.child(mkey).child("key").setValue(mkey);
//                        reference.child("count").setValue(count);

                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Toast.makeText(getActivity(),"評論成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //image upload
        list = new ArrayList<>();
        picture_btn = root.findViewById(R.id.picture_btn);
        picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_CODE);
            }
        });

        //video upload
        video_btn = root.findViewById(R.id.video_btn);
        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getActivity());
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIDEO);

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews").child(String.valueOf(id)).child("files").child(mkey);

        //image
        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                Toast.makeText(getContext(), "Multiple", Toast.LENGTH_SHORT).show();
                int total_item = data.getClipData().getItemCount();

                for (int i = 0; i < total_item; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagename = getFileName(imageUri);

                    homemodel homemodel = new homemodel(imagename, imageUri);
                    list.add(homemodel);

                    bookingReviewAdapter = new BookingReviewAdapter(getActivity(), list);

                    picture_rv.setAdapter(bookingReviewAdapter);

                    StorageReference mRef = storageReference.child("review_image").child(user.getUid()).child(String.valueOf(id)).child(imagename);

                    mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getContext(), "Done" ,Toast.LENGTH_SHORT).show();
                            mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            reference.child("image_review" + j).setValue(uri.toString());
                                            j++;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else if (data.getData() != null) {
                Toast.makeText(getContext(), "single", Toast.LENGTH_SHORT).show();
                Uri imageUri = data.getData();
                String imagename = getFileName(imageUri);

                homemodel homemodel = new homemodel(imagename, imageUri);
                list.add(homemodel);

                bookingReviewAdapter = new BookingReviewAdapter(getActivity(), list);

                picture_rv.setAdapter(bookingReviewAdapter);
                review_confirm = getActivity().findViewById(R.id.review_confirm);

                StorageReference mRef = storageReference.child("review_image").child(user.getUid()).child(String.valueOf(id)).child(imagename);

                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getContext(), "Done" ,Toast.LENGTH_SHORT).show();
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child("image_review0").setValue(uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}