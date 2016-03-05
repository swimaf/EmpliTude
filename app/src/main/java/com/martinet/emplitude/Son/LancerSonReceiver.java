package com.martinet.emplitude.Son;
/**
 * Created by martinet on 04/02/16.
 */

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
import com.martinet.emplitude.Outil.EvenementInternet;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class LancerSonReceiver extends BroadcastReceiver {

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private Context context;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.preference = context.getSharedPreferences(Constants.PREFERENCE_SON, 0);
        this.editor = preference.edit();

        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        editor.putInt("notificationOld", amanager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        editor.putInt("sonnerieOld", amanager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        editor.putInt("musicOld", amanager.getStreamVolume(AudioManager.STREAM_MUSIC));
        editor.commit();
        amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, preference.getInt("notification", 0), AudioManager.ADJUST_LOWER);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerie", 0), AudioManager.ADJUST_LOWER);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, preference.getInt("music", 0), AudioManager.ADJUST_LOWER);


        SimpleDateFormat h = new SimpleDateFormat("dd HH:mm", Locale.FRENCH);
        //TEST
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_son)
                        .setContentTitle("Empli'tude")
                        .setContentText("Mode cours activé");
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

