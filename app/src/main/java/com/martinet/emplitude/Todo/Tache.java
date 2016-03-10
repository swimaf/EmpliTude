package com.martinet.emplitude.Todo;

import com.martinet.emplitude.Emploi.Cours;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Flo on 04/01/2016.
 */
public class Tache implements Serializable{

    private static int SERIAL = 0;
    private int id;
    private String nom;
    private Cours cours;
    private Date date;

    public Tache (String nom, Cours cours, Date date){
        this.nom = nom;
        this.date = date;
        this.cours = cours;
        this.id = SERIAL;
        SERIAL++;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Cours getCours() { return cours; }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId(){ return this.id;}
}