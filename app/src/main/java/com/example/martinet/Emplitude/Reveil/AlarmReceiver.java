package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */
/*
Classe qui defini les action a faire au moment ou le reveil sonne
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.io.IOException;


public class AlarmReceiver extends WakefulBroadcastReceiver {

    //------Variables------//
    public static MediaPlayer mMediaPlayer;
    public static Ringtone ringtone;
    public static Vibrator v;
    private Thread thread;

    private SharedPreferences sharedpreferences;

    public void onReceive(Context context, Intent intent) {

        //-----Faire jouer le son-----//
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        this.sharedpreferences = context.getSharedPreferences(ReveilActivity.MesPREFERENCES, Context.MODE_PRIVATE);

        /*
        ringtone.setAudioAttributes(AudioAttributes.USAGE_ALARM);
        //ringtone.setStreamType(AudioManager.STREAM_ALARM);
        ringtone = RingtoneManager.getRingtone(context, alert);
        ringtone.play();*/

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

       // audioManager.setStreamVolume(AudioManager.STREAM_ALARM, aug, 0);


        try {
            mMediaPlayer.setDataSource(context, alert);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        mMediaPlayer.start();

            //-----Faire jouer le vibreur-----//
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (sharedpreferences.getBoolean(ReveilActivity.keyDSonner, true)) {
            long[] pattern = {0, 2000, 1000};
            v.vibrate(pattern, 0);
        }
            //------Lancer l'ouverture de la page sonnerie -----//
        Intent intentone = new Intent(context.getApplicationContext(), SonnerieActivity.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentone);

    }

}

