package com.martinet.emplitude.Models;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Tool.Resource;
import com.martinet.emplitude.Tool.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 12/08/16.
 */

public class Group implements Serializable{

    final static private String URL_GROUPS = Constants.API_URL+"/groups";

    private String name;
    private String identifiant;
    private Boolean authentification;
    private String pseudo;
    private String password;
    private School school;

    public Group(String name, String identifiant, Boolean authentification) {
        this.name = name;
        this.identifiant = identifiant;
        this.authentification = authentification;
    }

    public static List<Group> parseJson(String arrayJson){
        JSONArray json;
        List<Group> schools = new ArrayList<>();
        String name, identifiant;
        Boolean authentification;
        try {
            json = new JSONArray(arrayJson);
            for(int i=0; i<json.length(); i++) {
                name = json.getJSONObject(i).getString("name");
                identifiant = json.getJSONObject(i).getString("identifiant");
                authentification =json.getJSONObject(i).getBoolean("authentification");
                schools.add(new Group(name, identifiant, authentification));
            }
        } catch (JSONException ignored) {}
        return schools.isEmpty() ? null : schools;
    }

    public static void executeURL(Response retour, School school) {
        try {
            new Resource(retour).execute(new URL(URL_GROUPS+"/"+school.getId()));
        } catch (Exception e) {
            retour.onError("L'url est mal formée.");
        }

    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public void setAuthentification(Boolean authentification) {
        this.authentification = authentification;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public Boolean getAuthentification() {
        return authentification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean equals(Object o){
        return ((Group) o).getIdentifiant().equals(identifiant);
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
