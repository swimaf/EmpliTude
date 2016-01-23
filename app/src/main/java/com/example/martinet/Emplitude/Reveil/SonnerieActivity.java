package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
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

import static com.example.martinet.Emplitude.Reveil.AlarmReceiver.*;

/**
 * Created by Arnaud Regnier on 29/12/15.
 */
/*
classe en interaction avec l'ihm, tout ce qui ce passe pendant que le reveil sonne
 */
public class SonnerieActivity extends Activity {
    Button brStop;
    Button brTempo;
    TextView tvrHeure;
    public static final String MesPREFERENCES = "mesPreferences";
    public static SharedPreferences sharedpreferences;
    private ArreterAlarm arreterAlarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    // public static SharedPreferences sharedpreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sonnerie_activity);
        init();
        listeners();

        //repeter();
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
    public void arreterAlarm(){

        arreterAlarm.cancelAlarmEtSetReveil();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(ReveilActivity.keyNbRepetitionRestante, 0);
        editor.commit();
        couperSonnerie();
    }

    //----------------------------------------------------------------
    //-------------Methode pour initialiser les variables-------------
    //----------------------------------------------------------------
    public void init(){
        sharedpreferences = getSharedPreferences(MesPREFERENCES, Context.MODE_PRIVATE);
        brStop = (Button) findViewById(R.id.brStop);

        //AlarmManager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Itent
        Intent myIntent = new Intent(SonnerieActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SonnerieActivity.this, 0, myIntent, 0);



        arreterAlarm = new ArreterAlarm(SonnerieActivity.this,alarmManager,pendingIntent);
        //Bouton repeter
        brTempo = (Button) findViewById(R.id.brRepeterFinal);
        int tmp = sharedpreferences.getInt(ReveilActivity.keyMinTempo, 0);
        brTempo.setText("Temporiser " +tmp+ " min");

        tvrHeure = (TextView) findViewById(R.id.tvrHeure);
        SimpleDateFormat heure = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");
            Calendar c = Calendar.getInstance();
            // Je recup heure minute actuel
            String ha = heure.format(c.getTime());
            String ma = minute.format(c.getTime());
        tvrHeure.setText(""+ha+":"+ma);

    }
    //----------------------------------------------------------------
    //-------------Methode pour initialiser les listeners-------------
    //----------------------------------------------------------------
    public void listeners() {
        //Listener arreter
        View.OnClickListener listenerStop = new View.OnClickListener() {
            public void onClick(View view) {

                arreterAlarm();
                ouvrirMain();
                //penser a onDelete ?
            }
        };

        brStop.setOnClickListener(listenerStop);


        //Listener Tempo
        View.OnClickListener listenerTempo = new View.OnClickListener() {
            public void onClick(View view) {
                arreterAlarm();
                ReveilActivity.setAlarmTempo();
                ouvrirMain();
                //penser a onDelete ?
            }
        };

        brTempo.setOnClickListener(listenerTempo);


    }
    //-------------------------------------------------------------------------------------------
    //--methode pour faire sonner le temps souhaiter par l'utilisateur puis couper la sonnerie.--
    //-------------------------------------------------------------------------------------------
   /* public void repeter(){
        if(sharedpreferences.getInt(ReveilActivity.keyNbRepetitionRestante,0)!=0){
            final int tmp = (sharedpreferences.getInt(ReveilActivity.keyMinRepetition,0))*60000;//60000 c'est 1mn en ms
            Thread temspAttente = new Thread(){
                public void run() {
                    try {
                        sleep(tmp);
                        arreterAlarm();
                        reveilActivity.setAlarmRepeter();
                        ouvrirMain();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            temspAttente.start();
        }
    }*/
}