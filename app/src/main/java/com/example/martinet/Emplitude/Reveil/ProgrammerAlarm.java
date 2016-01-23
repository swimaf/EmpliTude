package com.example.martinet.Emplitude.Reveil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Emploi.ADE_information;
import com.example.martinet.Emplitude.Emploi.Cour;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by root on 23/01/16.
 */
public class ProgrammerAlarm {
    private ADE_information adeInfo;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Cour cour;

    public ProgrammerAlarm(Context c,AlarmManager a,PendingIntent p){
        adeInfo = new ADE_information(c);
        alarmManager = a;
        pendingIntent = p;
        cour = new Cour();
    }

    //==================================================================
    //==================================================================
    //==========================SETTER D'ALARM==========================
    //==================================================================
    //==================================================================
    public void setAlarmAuto() {

        Calendar calendar = Calendar.getInstance();

        Date dSonner = new Date();
        Date dactu = new Date();
        Calendar cal = GregorianCalendar.getInstance();
        Calendar calActu = GregorianCalendar.getInstance();

        Date d = new Date();
        d=null;
        while(d==null){ // Tant que la le premier cour de la journee n'existe pas, on va sur le jour suivant ATTENTION SI IL YA  PAS DE JOUR SUIVANT BOUCLE INFINI

                System.out.println("Je suis rentré dans la boucle null");
                cour = adeInfo.getFirstBYDate(dSonner);
                System.out.println(cour);
                if(cour!=null) {
                    System.out.println("Je suis dans le if");
                    d = adeInfo.getFirstBYDate(dSonner).getDateD();
                }// on recup l'heure du premier cour

            dSonner.setTime(dSonner.getTime() + 86400000); // On passe a la journee d'apres
        }
        System.out.println("Je suis sorti de la boucle null");
        cal.setTime(d); // on defini la date ou il doit sonner
        calActu.setTime(dactu);
        if(cal.get(Calendar.HOUR_OF_DAY)>=calActu.get(Calendar.HOUR_OF_DAY)){// Si la premiere heure de cour de la journee est passer alors on programme le reveil sur la premiere heure du jour suivant
            dSonner.setTime(dSonner.getTime() + 86400000);// On passe au jour d'apres

                d = adeInfo.getFirstBYDate(dSonner).getDateD(); // on recup l'heure du cour

            while(d==null){ // Tant que la le premier cour de la journee n'existe pas, on va sur le jour suivant ATTENTION SI IL YA  PAS DE JOUR SUIVANT BOUCLE INFINI

                System.out.println("Je suis rentré dans la boucle null");
                cour = adeInfo.getFirstBYDate(dSonner);
                System.out.println(cour);
                if(cour!=null) {
                    System.out.println("Je suis dans le if");
                    d = adeInfo.getFirstBYDate(dSonner).getDateD();
                }// on recup l'heure du premier cour

                dSonner.setTime(dSonner.getTime() + 86400000); // On passe a la journee d'apres
            }
            cal.setTime(d); // On re met a jour l'objet calandar qui contient la date ou il doit sonner
        }



        //Met dans un object calendar l'heure ou il doit sonner puis la minute


        calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));


       /* //Set les repetitions
        nbRepetionRestante = getNbFoisRepeter();*/
       /*
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(keyNbRepetitionRestante, nbRepetionRestante);
        editor.commit();*/

        //Set l'alarme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Choix en fonction de la version d'android
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
        int tempo = cal.get(Calendar.HOUR_OF_DAY);
        int tempoM = cal.get(Calendar.MINUTE);

        String s = String.valueOf(tempo+":"+tempoM);
        Log.i("alarm auto set", s);
        //setAlarmRepeter();
    }




}
