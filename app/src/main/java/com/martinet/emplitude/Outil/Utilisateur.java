package com.martinet.emplitude.Outil;

import java.io.Serializable;


public class Utilisateur implements Serializable{
    private String identifiant;

    public Utilisateur(String identifiant){
        this.identifiant = identifiant;
    }
    public String getIdentifiant(){
        return this.identifiant;
    }

}
