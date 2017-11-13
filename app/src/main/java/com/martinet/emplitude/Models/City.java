package com.martinet.emplitude.Models;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Tool.Resource;
import com.martinet.emplitude.Tool.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 12/08/16.
 */

public class City implements Serializable{

    final static private String URL_CITY = Constants.API_URL+"/cities";

    private String name;
    private String photo;

    public City(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public static List<City> parseJson(String arrayJson){
        JSONArray json;
        List<City> cities = new ArrayList<>();
        try {
            json = new JSONArray(arrayJson);
            for(int i=0; i<json.length(); i++) {
                cities.add(new City(json.getJSONObject(i).getString("name"), json.getJSONObject(i).getString("photo")));
            }
        } catch (JSONException ignored) {}
        return cities.isEmpty() ? null : cities;
    }

    public static void executeURL(Response retour) {
        try {
            new Resource(retour).execute(new URL(URL_CITY));
        } catch (Exception e) {
            retour.onError("L'url est mal formée."+e.getMessage());
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object city) {
        return ((City) city).name.equals(name);
    }
}
