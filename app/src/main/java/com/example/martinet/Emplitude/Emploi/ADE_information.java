package com.example.martinet.Emplitude.Emploi;

/**
 * Created by martinet on 11/11/15.
 */

import android.content.Context;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Outil.Fichier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

public class ADE_information {

    private Date date;
    private Boolean vide;
    private Vector<Cour> cours;
    private Vector<Object> allCours;
    private SimpleDateFormat dateFormat;
    private Context context;
    private int compteur;


    public ADE_information(Date date, Context context){
        this.context = context;
        this.date = date;
        this.init();
    }
    public ADE_information(Context context){
        this.context = context;
        this.init();
    }

    public void init(){
        dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        allCours = Fichier.readAll(Constants.courFile, context);
        if(allCours != null){
            this.vide = false;
        }else{
            this.vide = true;
        }
    }
    //Recupération des cours
    public Vector<Cour> getCours() {
        this.get();
        if(this.vide){
            return null;
        }
        return cours;
    }

    //Récupération dernier cour
    public Cour getNext(){
        Cour c;
        cours = new Vector<>();
        Date d = new Date();
        for(int i =0; i<allCours.size(); i++){
            c = (Cour)allCours.get(i);
            if(d.before(c.getDateD())) {
                this.cours.add(c);
            }
        }
        Collections.sort(this.cours, new Comparator<Object>() {
            public int compare(Object m1, Object m2) {
                Date d = (((Cour) m1).getDateD());
                Date d2 = ((Cour) m2).getDateD();
                return d.compareTo(d2);
            }
        });
        return this.cours.get(0);
    }

    //Récupération tous les cours par date
    public void get(){
        this.cours = new Vector<>();
        Cour c;
        Jour j = new Jour(this.date);
        Jour j2;
        for(int i =0; i<allCours.size(); i++){
            c = (Cour)allCours.get(i);
            j2 = new Jour(c.getDateD());
            if(j.getDateJour().equals(j2.getDateJour())) {
                this.cours.add(c);
            }
        }
    }

    public Cour getFirstBYDate(Date date) {
        this.date = date;
        this.get();

        Collections.sort(this.cours, new Comparator<Object>() {

            public int compare(Object m1, Object m2) {
                Date d = (((Cour) m1).getDateD());
                Date d2 = ((Cour) m2).getDateD();
                return d.compareTo(d2);

            }
        });
        if (this.cours.size() == 0) {
            return null;
        } else {
            return this.cours.get(0);
        }
    }
}
