package com.martinet.emplitude.Emploi;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe d'information d'un cours implementant serializable pour la sauvegarde des données
 */
public class Cours implements Serializable{

    private String matiere;
    private Date dateD;
    private Date dateF;
    private String prof;
    private String salle;
    private String resumer;

    public Cours(String resumer, String matiere, Date dateD, Date dateF, String prof, String salle){
        this.matiere = matiere;
        this.resumer = resumer;
        this.dateD = dateD;
        this.dateF = dateF;
        this.prof = prof;
        this.salle = salle;
    }
    public Cours(){}
    public String getMatiere() {
        return matiere;
    }
    public String getResumer() {
        return resumer;
    }

    public Date getDateD() {
        return dateD;
    }

    public Date getDateF() {
        return dateF;
    }

    public String getProf() {
        return prof;
    }

    public String getSalle() {
        return salle;
    }
}
