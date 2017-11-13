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

public class School implements Serializable{

    final static private String URL_SCHOOLS = Constants.API_URL+"/schools";

    private Integer id;
    private String name;
    private String url;
    private City city;

    public School(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public static List<School> parseJson(String arrayJson){
        JSONArray json;
        List<School> schools = new ArrayList<>();
        try {
            json = new JSONArray(arrayJson);
            for(int i=0; i<json.length(); i++) {
                schools.add(new School(json.getJSONObject(i).getInt("id"), json.getJSONObject(i).getString("name"), json.getJSONObject(i).getString("url")));
            }
        } catch (JSONException ignored) {}
        return schools.isEmpty() ? null : schools;
    }

    public static void executeURL(Response retour, City city) {
        try {
            String encodedURL = URLEncoder.encode(city.getName(),"UTF-8").replace("+", "%20");
            new Resource(retour).execute(new URL(URL_SCHOOLS+"/"+encodedURL));
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

    public String toString() {
        return name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
