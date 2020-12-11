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
import android.util.Log;
import android.util.Pair;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotificationManager;
    private static AlarmManager alarmManager;

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
                R.id.nav_recipes,
                R.id.scanBarcodeFragment,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Notifications
        cancelAllAlarms(this); //delete all pending intents here?
        createNotificationChannel();
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        List<Pair<Integer, Integer>> alarms = Preferences.getNotificationsHours(this, this);
        //long repeatInterval = AlarmManager.INTERVAL_DAY;//15000;//AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        setNotifications(this, alarms, NOTIFICATION_ID);

        //get notifications intent to go to wannaEat
        Intent intent = getIntent();
        try{
            String action = intent.getAction().toUpperCase();
            String message = intent.getStringExtra("TODETAIL");
            if(action != null){
                if(action.equalsIgnoreCase(getResources().getString(R.string.notification_action)) ||
                        message.equalsIgnoreCase(getResources().getString(R.string.notification_action))){
                    Log.d("notifiIntent", "Intent was " + intent.toString());
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_wannaEatFragment);
                }else{
                    if(action.equalsIgnoreCase(getResources().getString(R.string.notification_action2)) ||
                            message.equalsIgnoreCase(getResources().getString(R.string.notification_action2))){
                        Log.d("notifiIntent", "Intent was " + intent.toString());
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_recipeDetailFragment);
                    }
                }
            }else{
                Log.d("notifiIntent", "Intent was null");
            }
        }catch(Exception e){
            Log.e("notifiIntent", "Problem consuming action from intent", e);
        }
    }

    public static void setNotifications(Context context, List<Pair<Integer, Integer>> alarms, int id){
        long repeatInterval = AlarmManager.INTERVAL_DAY;
        int i = 0;

        while(i < alarms.size()){
            //check if 5 intents are needed
            Intent notifyIntent = new Intent(context, AlarmReceiver.class);
            setAlarm(setNotifyAlarm(alarms.get(i).first, alarms.get(i).second),
                    repeatInterval, id + i, notifyIntent, context);
            i++;
        }

    }

    private static void setAlarm(Calendar time, long repeatingTime, int pk, Intent alarmIntent, Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pk, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if((Build.VERSION.SDK_INT >= 23 || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                && alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, time.getTimeInMillis(), repeatingTime, pendingIntent);
        }else { //notifications for older versions?
            manager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), repeatingTime, pendingIntent);
            alarmManager.cancel(pendingIntent);
        }
    }

    private void cancelAllAlarms(Context context){
        int countId = 0;
        // Cancel alarms
        while(true) {
            Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(context, countId, updateServiceIntent, 0);
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
                Log.e("Restart notifications", "AlarmManager update was not canceled. " + countId); //+ e.toString()
                return;
            }
            countId++;
        }
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
                            "Recipes ready",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies recipes");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}