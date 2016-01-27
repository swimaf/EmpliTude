package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */
/*
Classe qui defini les action a faire au moment ou le reveil sonne
 */

import android.content.Context;
import android.content.Intent;
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
    public static Ringtone ringtone;
    public static Vibrator v;
    private Thread thread;
    public static MediaPlayer mMediaPlayer;

    public void onReceive(Context context, Intent intent) {

        //-----Faire jouer le son-----//
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        /*
        ringtone.setAudioAttributes(AudioAttributes.USAGE_ALARM);
        //ringtone.setStreamType(AudioManager.STREAM_ALARM);
        ringtone = RingtoneManager.getRingtone(context, alert);
        ringtone.play();*/

         mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int aug = 0;
        System.out.println("valeur son" + aug);
       // audioManager.setStreamVolume(AudioManager.STREAM_ALARM, aug, 0);


            try {
                mMediaPlayer.setDataSource(context, alert);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(audioManager.STREAM_ALARM), 0);
            mMediaPlayer.start();


            //-----Faire jouer le vibreur-----//
            v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            if (ReveilActivity.getDSonner()) {
                long[] pattern = {0, 2000, 1000};
                v.vibrate(pattern, 0);
            }
            //------Lancer l'ouverture de la page sonnerie -----//
            Intent intentone = new Intent(context.getApplicationContext(), SonnerieActivity.class);
            intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentone);

        }

    }

