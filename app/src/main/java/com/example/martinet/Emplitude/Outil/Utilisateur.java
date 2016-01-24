package com.example.martinet.Emplitude.Outil;

import java.io.Serializable;


public class Utilisateur implements Serializable{
    private String identifiant;
    private String type;

    public Utilisateur(String identifiant, String type){
        this.identifiant = identifiant;
        this.type = type;
    }
    public String getIdentifiant(){
        return this.identifiant;
    }
    public String toString(){
        return "Id : "+identifiant+" Type"+type;
    }
}
