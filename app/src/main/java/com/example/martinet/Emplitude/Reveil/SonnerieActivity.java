package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.Outil.OnSwipeTouchListener;
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






    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sonnerie_activity);
        init();
        listeners();
        repeter();

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
        ReveilActivity.cancelAlarm();
        couperSonnerie();
    }
    //----------------------------------------------------------------
    //-------------Methode pour initialiser les variables-------------
    //----------------------------------------------------------------
    public void init(){
        brStop = (Button) findViewById(R.id.brStop);

        //Bouton repeter
        brTempo = (Button) findViewById(R.id.brRepeterFinal);
        brTempo.setText("Temporiser " + ReveilActivity.getMinTempo() + " min");

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
    public void repeter(){
        if(ReveilActivity.nbRepetionRestante!=0){
            final int tmp = (ReveilActivity.minRepetition)*60000;//60000 c'est 1mn en ms
            Thread temspAttente = new Thread(){
                public void run() {
                    try {
                        sleep(tmp);
                        arreterAlarm();
                        ReveilActivity.setAlarmRepeter();
                        ouvrirMain();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            temspAttente.start();
        }
    }
}