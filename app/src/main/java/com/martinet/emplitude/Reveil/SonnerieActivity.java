package com.martinet.emplitude.Reveil;
/**
 * Created by Arnaud on 04/01/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arnaud Regnier on 29/12/15.
 */
/*
classe en interaction avec l'ihm, tout ce qui ce passe pendant que le reveil sonne
 */
public class SonnerieActivity extends Activity {
    //----Variables----//
    private Button brStop;
    private Button brTempo;
    private TextView tvrHeure;
    private ArreterAlarm arreterAlarm;
    private ProgrammerAlarm programmerAlarm;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public SharedPreferences sharedpreferences;
    private AudioManager audioManager;
    private MediaPlayer mMediaPlayer;
    private FlashArt flashArt;

    private int i = 1;

    //---OnCreate----//
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sonnerie_activity);
        init();
        listeners();
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            String chemain = "/storage/sdcard0/BlackBerry/music/"+sharedpreferences.getString(ReveilActivity.keySonChoisis,"Défaut");
            mMediaPlayer.setDataSource(this,alert);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);

        if (sharedpreferences.getBoolean(ReveilActivity.keyDFondu, true)) {
            for (i = 1; i < audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM); i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("je suis dans le run" + i);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);
            }
        }
        /*
        if (sharedpreferences.getBoolean(ReveilActivity.keyDFondu, true)) {

            int tps = (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)) * 1000;
            int change = (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * 1000);
            Date actu = new Date();
            Date dateFin = new Date();
            dateFin.setTime(actu.getTime() + tps);
            long aug = 0;
            while (dateFin.getTime() != actu.getTime()) {
                Date tempo = new Date();
                System.out.println("je suis dans le while" + aug + " " + change);
                aug = (dateFin.getTime()) - ((tempo.getTime()));
                if (aug < change) {
                    System.out.println("je suis dans le if" + aug + " " + change);
                    i++;
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);
                    aug = 0;
                    change = change - 1000;
                }
                actu.setTime(tempo.getTime());
            }
        }*/
        mMediaPlayer.start();

    }
    //----------------------------------------------------------------
    //-------------Methode pour ouvrir le MainActivity----------------
    //----------------------------------------------------------------
    public void ouvrirMain(){

        Intent it = new Intent(SonnerieActivity.this,MainActivity.class);
        SonnerieActivity.this.startActivity(it);
    }
    //-----------------------------------------------------------------
    //--------------Methode pour arreter l'alarme----------------------
    //-----------------------------------------------------------------
    public void arreterAlarm()
    {
        arreterAlarm.cancelAlarmEtSetReveil();
        mMediaPlayer.stop();
        if(AlarmReceiver.v.hasVibrator()) {
            AlarmReceiver.v.cancel();
        }
        if (sharedpreferences.getBoolean(ReveilActivity.keyDTorche, true)) {
            if (Build.VERSION.SDK_INT >= 21) {
                flashArt.turnOffFlash(getBaseContext());
            }
        }
    }
    //----------------------------------------------------------------
    //-------------Methode pour initialiser les variables-------------
    //----------------------------------------------------------------
    public void init(){
        sharedpreferences = getSharedPreferences(ReveilActivity.MesPREFERENCES, Context.MODE_PRIVATE);
        brStop = (Button) findViewById(R.id.brStop);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(SonnerieActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SonnerieActivity.this, 0, myIntent, 0);
        programmerAlarm = new ProgrammerAlarm(SonnerieActivity.this,alarmManager,pendingIntent);
        arreterAlarm = new ArreterAlarm(SonnerieActivity.this,alarmManager,pendingIntent);
        brTempo = (Button) findViewById(R.id.brRepeterFinal);
        int tmp = sharedpreferences.getInt(ReveilActivity.keyMinTempo, 0);
        brTempo.setText("Temporiser " +tmp+ " min");
        tvrHeure = (TextView) findViewById(R.id.tvrHeure);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        tvrHeure.setText(simpleDateFormat.format(c.getTime()));
    }
    //----------------------------------------------------------------
    //-------------LISTENERS------------------------------------------
    //----------------------------------------------------------------
    public void listeners() {
        //-----Bouton arreter----//
        View.OnClickListener listenerStop = new View.OnClickListener() {
            public void onClick(View view) {

                arreterAlarm();
                ouvrirMain();
            }
        };

        brStop.setOnClickListener(listenerStop);
        //-----Bouton temporiser----//
        View.OnClickListener listenerTempo = new View.OnClickListener() {
            public void onClick(View view) {
                arreterAlarm();
                programmerAlarm.setAlarmTempo();
                ouvrirMain();
            }
        };
        brTempo.setOnClickListener(listenerTempo);
    }
}