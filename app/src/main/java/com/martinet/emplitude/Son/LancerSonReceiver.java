package com.martinet.emplitude.Son;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import com.martinet.emplitude.Constants;

/**
 * Récepteur qui se déclenche au début d’un cours
 */

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
        editor.putInt("vibratorOld", amanager.getRingerMode());
        editor.commit();

        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("sonnerie", 0), AudioManager.ADJUST_LOWER);
        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("notification", 0), AudioManager.ADJUST_LOWER);
        amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, preference.getInt("music", 0), AudioManager.ADJUST_LOWER);
        if(preference.getBoolean("vibrator", true)){
            amanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else{
            amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }

    }


}

