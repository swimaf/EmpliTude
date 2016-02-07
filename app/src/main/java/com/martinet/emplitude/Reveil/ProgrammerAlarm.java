package com.martinet.emplitude.Reveil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;


import com.martinet.emplitude.Emploi.ADE_information;
import com.martinet.emplitude.Emploi.Cour;

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
    private Context context;
    public static final String MesPREFERENCES = "mesPreferences";
    public static SharedPreferences sharedpreferences;

    public ProgrammerAlarm(Context c,AlarmManager a,PendingIntent p){
        adeInfo = new ADE_information(c);
        alarmManager = a;
        pendingIntent = p;
        context = c;
        cour = new Cour();
        sharedpreferences = c.getSharedPreferences(MesPREFERENCES, Context.MODE_PRIVATE);

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
        boolean jpasser = false; // si jpasser == true ca veut dire que on est passer sur le jour d'apres aujourdhui

        Date d = new Date();
        d=null;
        int i = 0;
        while(d==null && i <= 100){ // Tant que la le premier emploi_cour de la journee n'existe pas, on va sur le jour suivant ATTENTION SI IL YA  PAS DE JOUR SUIVANT BOUCLE INFINI
        i++;
                System.out.println("Je suis rentré dans la boucle null");
                cour = adeInfo.getFirstBYDate(dSonner);
                System.out.println(cour);
                if(cour!=null) {
                    System.out.println("Je suis dans le if");
                    d = adeInfo.getFirstBYDate(dSonner).getDateD();
                }// on recup l'heure du premier emploi_cour

            dSonner.setTime(dSonner.getTime() + 86400000); // On passe a la journee d'apres
            jpasser = true;
        }
        System.out.println("Je suis sorti de la boucle null");
        int tps = sharedpreferences.getInt(ReveilActivity.keyNpDureePrepa, 0);
        cal.setTime(d); // on defini la date ou il doit sonner
        cal.add(Calendar.MINUTE, -tps);

        calActu.setTime(dactu);
        if((cal.get(Calendar.HOUR_OF_DAY)<=calActu.get(Calendar.HOUR_OF_DAY))){// Si la premiere heure de emploi_cour de la journee est passer alors on programme le reveil sur la premiere heure du jour suivant
            System.out.println("je suis passer dans le deuxieme if");
           // dSonner.setTime(dSonner.getTime() + 86400000);// On passe au jour d'apres

                d = adeInfo.getFirstBYDate(dSonner).getDateD(); // on recup l'heure du emploi_cour

            while(d==null){ // Tant que la le premier emploi_cour de la journee n'existe pas, on va sur le jour suivant ATTENTION SI IL YA  PAS DE JOUR SUIVANT BOUCLE INFINI

                System.out.println("Je suis rentré dans la boucle null2");
                cour = adeInfo.getFirstBYDate(dSonner);
                System.out.println(cour);
                if(cour!=null) {
                    System.out.println("Je suis dans le if2");
                    d = adeInfo.getFirstBYDate(dSonner).getDateD();
                }// on recup l'heure du premier emploi_cour

                dSonner.setTime(dSonner.getTime() + 86400000); // On passe a la journee d'apres
            }
            int tps2 = sharedpreferences.getInt(ReveilActivity.keyNpDureePrepa, 0);
            cal.setTime(d); // On re met a jour l'objet calandar qui contient la date ou il doit sonner
            cal.add(Calendar.MINUTE, -tps2);

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
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        }
        int tempo = cal.get(Calendar.HOUR_OF_DAY);
        int tempoM = cal.get(Calendar.MINUTE);
        int tempoJ = cal.get(Calendar.DAY_OF_MONTH);
        int tempoMo = cal.get(Calendar.MONTH);
      /*  String Mon = "";
        switch (tempoMo){
            case 0 : Mon = "Janvier";
                break;
            case 1 : Mon = "Fevrier";
                break;
            case 2 : Mon = "Mars";
                break;
            case 3 : Mon = "Avril";
                break;
            case 4 : Mon = "Mai";
                break;
            case 5 : Mon = "Juin";
                break;
            case 6 : Mon = "Juillet";
                break;
            case 7 : Mon = "Aout";
                break;
            case 8 : Mon = "Septembre";
                break;
            case 9 : Mon = "Octobre";
                break;
            case 10 : Mon = "Novembre";
                break;
            case 11 : Mon = "Decembre";
                break;
            default:
                Mon = "";
                break;


        }



        String s = String.valueOf(tempo + ":" + tempoM + " -> jour :" + tempoJ);
        Log.i("alarm auto set", s);



        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_alarm)
                        .setContentTitle("Empli'tude")
                        .setContentText("Réveil programmé le "+tempoJ+" "+Mon+" à "+tempo+":"+tempoM);
        Intent resultIntent = new Intent(context, Accueil.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Accueil.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());

  */
    }


    public void setAlarmTempo() {
        int tps = sharedpreferences.getInt(ReveilActivity.keyMinTempo, 0);// Je recupere la valeur de tempo ( que j'ajouterais apres )
        SimpleDateFormat heure = new SimpleDateFormat("HH");
        SimpleDateFormat minute = new SimpleDateFormat("mm");

        Calendar c = Calendar.getInstance();
        // Je recup heure minute actuel
        String ha = heure.format(c.getTime());
        String ma = minute.format(c.getTime());
        // Conversion en int
        int entierHa = Integer.parseInt(ha);
        int entierMa = Integer.parseInt(ma);
        Log.i("heureAcctuel", ha);
        Log.i("minAcctuel", ma);

        if (entierMa + tps >= 60) {//Ex : si il est 6h58 que je tempo de 5mn, je peux pas dire je sonne a la 63ème minute
            entierMa = (entierMa + tps) - 60;// j'adiitionne j'obtiens 63, j'enleve 60, j'ai 3
            entierHa++;// je rajoute 1h car j'ai enlever 60mn
        } else {//sinon j'ai juste a ajouter les minute |ex : j'ajoute 5 mn si il esty 6h20
            entierMa = entierMa + tps;//20+5 = 25mn et les heures on pas changé
        }
        //variable tempo pour test console ( supprimable car fonctionne )
        String hap = String.valueOf(entierHa);
        String map = String.valueOf(entierMa);
        //affichage console
        Log.i("heureDeReport", hap);
        Log.i("minDeReport", map);
        Calendar calendar = Calendar.getInstance();
        //Met dans un object calendar l'heure ou il doit sonner puis la minute
        calendar.set(Calendar.HOUR_OF_DAY, entierHa);
        calendar.set(Calendar.MINUTE, entierMa);
        //Set l'alarme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Choix en fonction de la version d'android
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
    }



}
