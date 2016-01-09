package com.example.martinet.Emplitude.Emploi;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by martinet on 04/01/16.
 */

public class Cour implements Serializable{

    private String matiere;
    private Date dateD;
    private Date dateF;
    private String prof;
    private String salle;
    private String resumer;

    public Cour(String resumer, String matiere, Date dateD, Date dateF, String prof, String salle){
        this.matiere = matiere;
        this.resumer = resumer;
        this.dateD = dateD;
        this.dateF = dateF;
        this.prof = prof;
        this.salle = salle;
    }

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
