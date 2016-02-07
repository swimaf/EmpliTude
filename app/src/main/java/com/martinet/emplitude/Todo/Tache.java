package com.martinet.emplitude.Todo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Flo on 04/01/2016.
 */
public class Tache implements Serializable{

    private static int SERIAL = 0;
    private int id;
    private String nom, matiere;
    private Date date;

    public Tache (String nom, String matiere, Date date){
        this.nom = nom;
        this.date = date;
        this.matiere = matiere;
        this.id = SERIAL;
        SERIAL++;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatiere() { return matiere; }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId(){ return this.id;}
}