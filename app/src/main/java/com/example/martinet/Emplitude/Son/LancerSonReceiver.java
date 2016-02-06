package com.example.martinet.Emplitude.Son;

/**
 * Created by martinet on 04/02/16.
 */

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import android.content.BroadcastReceiver;

import android.content.SharedPreferences;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Emploi.ADE_information;


public class LancerSonReceiver extends BroadcastReceiver {

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public void onReceive(Context context, Intent intent) {
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

    }
}

