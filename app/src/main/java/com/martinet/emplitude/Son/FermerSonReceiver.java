package com.martinet.emplitude.Son;
/**
 * Created by martinet on 04/02/16.
 */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;

import android.content.BroadcastReceiver;

import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.Accueil;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Emploi.ADE_information;
import com.martinet.emplitude.Emploi.Cour;
import com.martinet.emplitude.Outil.EvenementInternet;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class FermerSonReceiver extends BroadcastReceiver {

    private SharedPreferences preference;
    private Context context;
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        this.preference = context.getSharedPreferences(Constants.PREFERENCE_SON, 0);

        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, preference.getInt("notificationOld", 0), AudioManager.ADJUST_LOWER);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerieOld", 0), AudioManager.ADJUST_LOWER);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, preference.getInt("musicOld", 0), AudioManager.ADJUST_LOWER);

        ADE_information ade_information =  new ADE_information(context);
        Cour prochainCour = ade_information.getNext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, LancerSonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent1, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCour.getDateD().getTime() - 60, pendingIntent);

        Intent intent2 = new Intent(context, FermerSonReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, prochainCour.getDateF().getTime() - 60, pendingIntent2);


        SimpleDateFormat h = new SimpleDateFormat("dd HH:mm", Locale.FRENCH);
        //TEST
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_son)
                        .setContentTitle("Empli'tude")
                        .setContentText("Mode cours déactivé");
        Intent resultIntent = new Intent(context, Accueil.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Accueil.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());

        receiver(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

    }
    public void receiver(int i){
        ComponentName receiver = new ComponentName(context, EvenementInternet.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, i, PackageManager.DONT_KILL_APP);
    }
}


