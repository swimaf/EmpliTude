package com.example.martinet.Emplitude.Todo;

import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Flo on 04/01/2016.
 */
public class Tache implements Serializable{

    private String nom, matiere;
    private Date date;
    private DateFormat dateF = DateFormat.getDateInstance(DateFormat.FULL);

    public Tache (String nom, String matiere, Date date){
        this.nom = nom;
        this.date = date;
        this.matiere = matiere;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getDate() {
        return dateF.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}