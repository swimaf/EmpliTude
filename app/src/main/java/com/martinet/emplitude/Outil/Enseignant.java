package com.martinet.emplitude.Outil;

/**
 * Classe héritant de utilisateur
 */
public class Enseignant extends Utilisateur {
    public Enseignant(String identifiant) {
        super(identifiant);
    }

    public String toString(){
        return "Enseignant";
    }
}
