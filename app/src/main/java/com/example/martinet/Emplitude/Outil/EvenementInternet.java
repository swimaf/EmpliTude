package com.example.martinet.Emplitude.Outil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.martinet.Emplitude.Emploi.ADE_automatique;


/**
 * Created by martinet on 02/01/16.
 */

public class EvenementInternet extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,ADE_automatique.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123456789, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent);
    }

}