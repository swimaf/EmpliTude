package com.martinet.emplitude.Sound;
/**
 * Created by martinet on 04/02/16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Initialization.Initialisation;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.R;


public class OnLessonBegin extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        SharedPreferences preference = context.getSharedPreferences(Constants.Preference.SOUND, 0);
        Lesson lesson = (Lesson) intent.getSerializableExtra("lesson");

        SharedPreferences.Editor editor = preference.edit();

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        editor.putInt("notificationOld", audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        editor.putInt("sonnerieOld", audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
        editor.putInt("musicOld", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        editor.putInt("vibrateOld", audioManager.getRingerMode());
        editor.commit();

        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, preference.getInt("notification", 0), AudioManager.ADJUST_SAME);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerie", 0), AudioManager.ADJUST_SAME);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preference.getInt("music", 0), AudioManager.ADJUST_SAME);

        if(preference.getBoolean("vibrer", true)) {
            audioManager.setMode(AudioManager.RINGER_MODE_VIBRATE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            audioManager.setMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }

        ProgrammSound.setReceiverOnLessonEnding(context, lesson);

        notification(context, " Active");
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


