package com.martinet.emplitude.Outil;

/**
 * Created by martinet on 02/02/16.
 */
public class Etudiant extends Utilisateur {

    private String groupe;

    public Etudiant(String identifiant,  String groupe){
        super(identifiant);
        this.groupe = groupe;
    }

    public String toString(){
        return "Etudiant  "+groupe;
    }
}
