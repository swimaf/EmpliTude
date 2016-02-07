package com.martinet.emplitude.Reveil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 23/01/16.
 */
public class ArreterAlarm {
    ProgrammerAlarm proAlarm;
    Context context;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent myIntent;


    public ArreterAlarm(Context c,AlarmManager a,PendingIntent p){
        proAlarm = new ProgrammerAlarm(c,a,p);
        myIntent = new Intent(c, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(c, 0, myIntent, 0);
        alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

    }

    public void cancelAlarmEtSetReveil() {
        proAlarm.setAlarmAuto();
        //nbRepetionRestante = 0;
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
