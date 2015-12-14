package com.example.martinet.drawer;

import java.io.Serializable;

/**
 * Created by martinet on 17/11/15.
 */
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
