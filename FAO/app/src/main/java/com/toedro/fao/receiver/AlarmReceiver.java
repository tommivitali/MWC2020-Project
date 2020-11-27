package com.toedro.fao.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.toedro.fao.MainActivity;
import com.toedro.fao.R;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context);
    }
    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, MainActivity.getNotificationId(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = getNotificationBuilder(context, contentPendingIntent);
        mNotificationManager.notify(MainActivity.getNotificationId(), builder.build());
    }
    private  NotificationCompat.Builder getNotificationBuilder(Context context, PendingIntent intent){
        return new NotificationCompat.Builder(context, MainActivity.getPrimaryChannelId())
                .setSmallIcon(R.drawable.ic_menu_recipes)
                .setContentTitle("Stand Up Alert")
                .setContentText("You should stand up and walk around now! " + MainActivity.getNotificationId())
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

}