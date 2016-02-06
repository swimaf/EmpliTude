package com.example.martinet.Emplitude.Son;

/**
 * Created by martinet on 04/02/16.
 */

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.content.WakefulBroadcastReceiver;


public class SonReceiver extends WakefulBroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        AudioManager amanager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

       /* //For notification
        amanager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND, AudioManager.ADJUST_LOWER);
        //For alarm
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        //For music
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        //For ringtone
        amanager.setStreamVolume(AudioManager.STREAM_RING, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

        //mute audio
        amanager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);*/
    }

}

