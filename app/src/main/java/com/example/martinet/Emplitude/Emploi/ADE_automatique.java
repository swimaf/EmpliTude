package com.example.martinet.Emplitude.Emploi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.example.martinet.Emplitude.Emploi.ADE_recuperation;
import com.example.martinet.Emplitude.Emploi.ADE_retour;

/**
 * Created by martinet on 02/01/16.
 */

public class ADE_automatique extends BroadcastReceiver implements ADE_retour{
    public void onReceive(Context context, Intent intent) {
        System.out.println("ddd");

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }

    @Override
    public void retour(int value) {
        if(value != ADE_recuperation.ERROR_ADE){

        }
    }
}
