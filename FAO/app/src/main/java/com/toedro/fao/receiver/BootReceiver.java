package com.toedro.fao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * The BootReceiver class reactivate AlarmReceiver class after a system reboot
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            AlarmReceiver.scheduleAlarms(context);
        }
    }
}
