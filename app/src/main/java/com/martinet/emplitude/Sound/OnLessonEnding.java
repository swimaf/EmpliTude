package com.martinet.emplitude.Sound;
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

import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.Constants;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Tool.EvenementInternet;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class OnLessonEnding extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        SharedPreferences preference = context.getSharedPreferences(Constants.Preference.SOUND, 0);

        AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, preference.getInt("notificationOld", 0), AudioManager.ADJUST_SAME);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerieOld", 0), AudioManager.ADJUST_SAME);
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, preference.getInt("musicOld", 0), AudioManager.ADJUST_SAME);

        amanager.setRingerMode(preference.getInt("vibrateOld", AudioManager.RINGER_MODE_SILENT));

        ProgrammSound.setReceiverOnLessonBegin(context);
        notification(context, "Désactivé");

    }

    private static void notification(Context context, String status) {
        /**
         * Notification
         */
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_son)
                        .setContentTitle("Empli'tude")
                        .setContentText("Mode cours"+status);
        Intent resultIntent = new Intent(context, Initialisation.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Initialisation.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());
    }

}

