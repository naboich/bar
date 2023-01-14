package com.example.bar.booking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bar.R;
import com.example.bar.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ReminderBroadcast extends BroadcastReceiver {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("booking");

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        Calendar calendar = Calendar.getInstance();
        int year_now = calendar.get(Calendar.YEAR);
        int month_now = calendar.get(Calendar.MONTH);
        int day_now = calendar.get(Calendar.DAY_OF_MONTH);
        int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
        int min_now = calendar.get(Calendar.MINUTE);
        month_now = month_now + 1;

        //
        int finalMonth_now = month_now;
        final int[] notificationId = {0x10};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    homemodel bar = dataSnapshot.getValue(homemodel.class);
                    int day = Integer.parseInt(bar.getDay());
                    int month = Integer.parseInt(bar.getMonth());
                    int year = Integer.parseInt(bar.getYear());
                    if (day - 1 == day_now && year == year_now && month == finalMonth_now && hour_now == 0 && min_now == 0) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification");
                        builder.setContentTitle("訂位提醒");
                        builder.setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(bar.getName() + "\n預約日期:" + bar.getMonth() + "月" + bar.getDay() + "日\n預約時間:" + bar.getHour() + ":" + bar.getMin() + "\n預約人數:" + bar.getPeople())
                        );
//                        builder.setContentText(bar.getMonth() + "月"  + bar.getDay() + "日 " + bar.getHour() + ":" + bar.getMin() + " " + bar.getName());
                        builder.setSmallIcon(R.mipmap.icon);
                        builder.setAutoCancel(true);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        managerCompat.notify(++notificationId[0], builder.build());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
