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

    //------Variables------//
    public static Ringtone ringtone;
    public static Vibrator v;

    public void onReceive(Context context, Intent intent) {

        //-----Faire jouer le son-----//
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, alert);
        ringtone.play();
        //-----Faire jouer le vibreur-----//
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if(ReveilActivity.getDSonner()) {
            long[] pattern = { 0, 2000, 1000 };
            v.vibrate(pattern,0);
        }
        //------Lancer l'ouverture de la page sonnerie -----//
        Intent intentone = new Intent(context.getApplicationContext(), SonnerieActivity.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentone);

    }

}
