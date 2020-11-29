package com.toedro.fao.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.toedro.fao.MainActivity;
import com.toedro.fao.R;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private int count = 0; //for debug

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context);
        count++;
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
                .setContentText("You should stand up read recipe now! " + count)
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
}