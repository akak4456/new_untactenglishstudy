package com.example.study.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.study.R;
import com.example.study.main.MainActivity;
import com.example.study.member.LoginActivity;
import com.example.study.timetable.TimeTableListActivity;

public class BroadcastD extends BroadcastReceiver {
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    public static final String CHANNEL_ID = "1001";
    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Long id = intent.getLongExtra("id",0L);
            Log.i("ALARM","ALARM"+intent.getStringExtra("title"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id.intValue(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(context,CHANNEL_ID);
            builder.setSmallIcon(R.drawable.icon_image).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setNumber(1).setContentTitle(intent.getStringExtra("title")).setContentText(intent.getStringExtra("detail"))
                    .setContentIntent(pendingIntent).setAutoCancel(true);
            notificationManager.notify(1, builder.build());
        }
    }
}
