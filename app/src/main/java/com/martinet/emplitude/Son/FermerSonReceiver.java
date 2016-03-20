package com.martinet.emplitude.Son;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Emploi.ADE_information;
import com.martinet.emplitude.Emploi.Cours;


/**
 * Récepteur qui se déclenche a la fin d’un cours
 */
public class FermerSonReceiver extends BroadcastReceiver {

    private SharedPreferences preference;
    public void onReceive(Context context, Intent intent) {


        this.preference = context.getSharedPreferences(Constants.PREFERENCE_SON, 0);

        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerieOld", 0), AudioManager.ADJUST_LOWER);
        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("notificationOld", 0), AudioManager.ADJUST_LOWER);
        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("musicOld", 0), AudioManager.ADJUST_LOWER);

        amanager.setRingerMode(preference.getInt("vibratorOld", 0));

        ADE_information ade_information =  new ADE_information(context);
        Cours prochainCours = ade_information.getNext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, LancerSonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent1, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCours.getDateD().getTime() - 60, pendingIntent);

        Intent intent2 = new Intent(context, FermerSonReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCours.getDateF().getTime() - 60, pendingIntent2);



    }
}


