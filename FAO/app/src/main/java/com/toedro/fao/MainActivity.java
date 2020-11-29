package com.toedro.fao;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.toedro.fao.receiver.AlarmReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotificationManager;
    private static AlarmManager alarmManager;
    private boolean alarmUp;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_homepage,
                R.id.nav_charts,
                R.id.nav_pantry,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Notifications
        createNotificationChannel();
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        int hour = 17, min = 45; //hour of the alarms
        long repeatInterval = AlarmManager.INTERVAL_DAY;//15000;//AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        Intent notifyIntent2 = new Intent(this, AlarmReceiver.class);
        Intent notifyIntent3 = new Intent(this, AlarmReceiver.class);

        setAlarm(setNotifyAlarm(hour, min), repeatInterval, NOTIFICATION_ID, notifyIntent);
        setAlarm(setNotifyAlarm(hour, min + 1), repeatInterval, NOTIFICATION_ID + 1,
                new Intent(this, AlarmReceiver.class));
        setAlarm(setNotifyAlarm(hour, min + 2), repeatInterval, NOTIFICATION_ID + 2, notifyIntent2);
        setAlarm(setNotifyAlarm(hour, min + 5), repeatInterval, NOTIFICATION_ID + 3, notifyIntent3);

        alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
    }

    public void setAlarm(Calendar time, long repeatingTime, int pk, Intent alarmIntent) {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pk, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if((Build.VERSION.SDK_INT >= 23 || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                && alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, time.getTimeInMillis(), repeatingTime, pendingIntent);
        }else { //notifications for older versions?
            manager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), repeatingTime, pendingIntent);
            alarmManager.cancel(pendingIntent);
        }
    }

    public void cancelAlarm(int pk, Intent notifyIntent) {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, pk, notifyIntent, 0);
        manager.cancel(pendingIntent);
    }

    public static String getPrimaryChannelId() {
        return PRIMARY_CHANNEL_ID;
    }
    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }
    public static Calendar setNotifyAlarm(int hour ,int min){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        if(Calendar.getInstance().after(calendar)){
            //if past Move to tomorrow
            calendar.add(Calendar.DATE, 1);
        }
        return calendar;
    }
    public void createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Notification channels are only available in OREO and higher. So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}