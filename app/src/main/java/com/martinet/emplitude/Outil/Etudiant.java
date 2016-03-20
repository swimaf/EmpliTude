package com.martinet.emplitude.Outil;

/**
 * Classe héritant de utilisateur avec un numéro de groupe en plus
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
