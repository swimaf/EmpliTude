package com.martinet.emplitude.Emploi;

import android.content.Context;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Jour;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


/**
 * Classe permettant de récupérer les différentes informations du fichier iCal
 */
public class ADE_information {

    private Date date;
    private Boolean vide;
    private Vector<Cours> cours;
    private Vector<Object> allCours;
    private Context context;


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
        allCours = Fichier.readAll(Constants.COURS_FILE, context);
        this.vide = allCours == null;
    }

    //Recupération des cours
    public Vector<Cours> getCours() {
        this.get();
        if(this.vide){
            return null;
        }
        return cours;
    }

    //Récupération dernier emploi_cour
    public Cours getNext(){
        Cours c;
        cours = new Vector<>();
        Date d = new Date();
        for(int i =0; i<allCours.size(); i++){
            c = (Cours)allCours.get(i);
            if(d.before(c.getDateD())) {
                this.cours.add(c);
            }
        }
        Collections.sort(this.cours, new Comparator<Object>() {
            public int compare(Object m1, Object m2) {
                Date d = (((Cours) m1).getDateD());
                Date d2 = ((Cours) m2).getDateD();
                return d.compareTo(d2);
            }
        });
        try {
            return this.cours.get(0);
        }catch (Exception e){
            return null;
        }
    }

    //Récupération de tous les cours par date après la date du jour
    public void get(){
        this.cours = new Vector<>();
        Cours c;
        Jour j = new Jour(this.date);
        Jour j2;
        for(int i =0; i<allCours.size(); i++){
            c = (Cours)allCours.get(i);
            j2 = new Jour(c.getDateD());
            if(j.getDateJour().equals(j2.getDateJour())) {
                this.cours.add(c);
            }
        }
    }

    //Récupération du premier cours d'une journée
    public Cours getFirstBYDate(Date date) {
        this.date = date;
        this.get();

        Collections.sort(this.cours, new Comparator<Object>() {

            public int compare(Object m1, Object m2) {
                Date d = (((Cours) m1).getDateD());
                Date d2 = ((Cours) m2).getDateD();
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
