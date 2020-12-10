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
        String action = "TOWANNAEAT";
        Intent contentIntent = new Intent(context, MainActivity.class);
        contentIntent.setAction(action);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, MainActivity.getNotificationId(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = getNotificationBuilder(context, contentPendingIntent);
        mNotificationManager.notify(MainActivity.getNotificationId(), builder.build());
    }

    private  NotificationCompat.Builder getNotificationBuilder(Context context, PendingIntent intent){
        return new NotificationCompat.Builder(context, MainActivity.getPrimaryChannelId())
                .setSmallIcon(R.drawable.ic_menu_recipes)
                .setContentTitle("Recipes ready")
                .setContentText("Click here to read your recipe now! ")
                .setContentIntent(intent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
    ////experimental stuff for let it stay after reboot/////////////
    public static void scheduleAlarms(Context context) {
        Calendar calendar = Calendar.getInstance();
        if (hasRunnedToday(context)) { // if the alarm has run this day
            calendar.add(Calendar.DATE, 1); // schedule it to run again starting
            // tomorrow
        }
        long firstRunTime = calendar.getTimeInMillis();
        AlarmManager mgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstRunTime,
                AlarmManager.INTERVAL_DAY, pi);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public static boolean hasRunnedToday(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("alarm_prefs", -1);
        long alarmLastRun = preferences.getLong("AlarmLastRun", -1);

        if(alarmLastRun == -1) {
            return false;
        }

        //check by comparing day, month and year
        Date now = new Date();
        Date lastRun = new Date(alarmLastRun);

        return now.getTime() - lastRun.getTime() < TimeUnit.DAYS.toMillis(1);
    }
}