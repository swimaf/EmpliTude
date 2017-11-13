package com.martinet.emplitude.Tool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.martinet.emplitude.Schelude.SchelureSelf;

/**
 * Receiver qui se déclanche lorsque l'utilisateur active internet
 */

public class EvenementInternet extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,SchelureSelf.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123456789, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent);
    }

}