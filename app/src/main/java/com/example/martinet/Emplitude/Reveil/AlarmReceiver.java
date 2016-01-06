package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */
/*
Classe qui defini les action a faire au moment ou le reveil sonne
 */

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    //Varianle qui fait sonner le reveil
    public static Ringtone ringtone;
    //MainActivity m;
    @Override
    public void onReceive(Context context, Intent intent) {

        //Afficher message de reveil sur l'appli ( adaptable sur SonnerieActivity ) -> ancien code
        //MainActivity.getTextView2().setText("Go en maths :p !");

        //Faire jouer la sonerie
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);//Le son qui va sonner ( de base j'ai mis ici TYPE_ALARM )

        ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();


        //Faire Virbrer le telephone
        if(ReveilActivity.getDSonner()) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(2000); // faire sonne en serie quand repeter
        }
        //m.lanceur();
        ReveilActivity.ouvrirSonnerieActivity();
        //INTENT PAR DESSUS L'ecran
        // repeter();
    }


    //methode pour couper le reveil ( appeler dans le mainactivity)
    public static void couperSonnerie(){
        ringtone.stop();
    }




}