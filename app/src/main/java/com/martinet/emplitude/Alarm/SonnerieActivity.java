package com.martinet.emplitude.Alarm;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SonnerieActivity extends Activity {

    private Button brStop;
    private TextView tvrHeure;
    private SharedPreferences sharedpreferences;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private Thread threadFondu;
    private Thread threadClose;
    private int i = 0;
    private Vibrator vibrator;
    private int maxVolume;
    private Button brReporter;
    private Boolean repeat = false;
    private boolean interrupt = false;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sonnerie_activity);
        initalization();
        listeners();
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        loadRington();
        loadVibrator();
        threadClose =  threadFondu = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5*60*1000);
                } catch (InterruptedException e) {}
                if(!interrupt) {
                    finish();
                    repeat = true;
                    ProgrammAlarm.reporterAlarm(SonnerieActivity.this, 10 * 60 * 1000);
                    threadClose.interrupt();
                }
            }
        });
        threadClose.start();
    }

    public void loadVibrator() {
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        if(sharedpreferences.getBoolean(ReveilActivity.KEY_VIBRATE, false)) {
            if(vibrator.hasVibrator()) {
                vibrator.vibrate(5 * 60 * 1000);
            }
        }
    }

    public void loadRington() {

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            mediaPlayer.setDataSource(this, alert);
            mediaPlayer.prepare();
        } catch (IOException ignored) {}

        mediaPlayer.setLooping(true);

        if(sharedpreferences.getBoolean(ReveilActivity.KEY_FONDU, false)) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);
            mediaPlayer.start();

            threadFondu = new Thread(new Runnable() {
                public void run() {
                    for(int i=0; i<maxVolume; i++) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {}
                        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_RAISE, 0);
                    }
                }
            });
            threadFondu.start();
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mediaPlayer.start();
        }


    }

    public void initalization(){
        sharedpreferences = getSharedPreferences(Constants.Preference.ALARM, Context.MODE_PRIVATE);
        brStop = (Button) findViewById(R.id.brStop);
        brReporter = (Button) findViewById(R.id.brReporter);
        tvrHeure = (TextView) findViewById(R.id.tvrHeure);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        tvrHeure.setText(simpleDateFormat.format(c.getTime()));
    }


    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        SonnerieActivity.this.startActivity(intent);
        finish();
    }

    public void stopAlarm() {
        if(threadFondu != null) {
            if(threadFondu.isAlive()) {
                threadFondu.interrupt();
            }
        }
        if(threadClose != null) {
            if(threadClose.isAlive()) {
                threadClose.interrupt();
            }
        }
        mediaPlayer.stop();
        if(vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }

    public void listeners() {
        brStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openMain();
            }
        });
        brReporter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
                repeat = true;
                ProgrammAlarm.reporterAlarm(SonnerieActivity.this, 10*60*1000);
            }
        });
    }

    public void onStop() {
        interrupt = true;
        stopAlarm();
        if(!repeat) {
            ProgrammAlarm.setAlarm(this);
        }
        super.onStop();

    }


}