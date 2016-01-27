package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static final String MesPREFERENCES = "mesPreferences";
    public static SharedPreferences sharedpreferences;

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
        AlarmReceiver.mMediaPlayer.stop();
        if(AlarmReceiver.v.hasVibrator())
            {
                 AlarmReceiver.v.cancel();
            }
    }
    //----------------------------------------------------------------
    //-------------Methode pour initialiser les variables-------------
    //----------------------------------------------------------------
    public void init(){
        sharedpreferences = getSharedPreferences(MesPREFERENCES, Context.MODE_PRIVATE);
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
        SimpleDateFormat heure = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");
            Calendar c = Calendar.getInstance();
            String ha = heure.format(c.getTime());
            String ma = minute.format(c.getTime());
        tvrHeure.setText(""+ha+":"+ma);
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