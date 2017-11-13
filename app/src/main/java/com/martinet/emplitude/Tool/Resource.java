package com.martinet.emplitude.Tool;

import android.os.AsyncTask;
import android.util.Base64;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;


public class Resource extends AsyncTask<URL, Void, String>{

    private Boolean nofail;
    private Response activity;
    private String username;
    private String password;

    public Resource(Response activity){
        this.nofail = true;
        this.activity = activity;
    }

    public Resource(Response activity, String username, String password){
        this.nofail = true;
        this.activity = activity;
        this.username = username;
        this.password = password;
    }

    protected String doInBackground(URL... params) {
        if(Constants.CONNECTED()) {
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) params[0].openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setRequestProperty("charset", "utf-8");

                if (username != null && password != null) {
                    String userCredentials = username + ":" + password;
                    String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));

                    urlConnection.setRequestProperty("Authorization", basicAuth);
                }

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    urlConnection.disconnect();
                    br.close();
                    nofail = false;
                    return sb.toString();
                } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return "Vous n'avez pas les permissions requise";
                } else {
                    return "Une erreur est survenue." + urlConnection.getResponseMessage() + urlConnection.getResponseCode();
                }
            } catch (UnknownHostException e) {
                return "Problème de connexion avec le site.";
            } catch (SocketTimeoutException e) {
                return "La connexion internet semble trop lente.";
            } catch (Exception e) {
                return e.getMessage();
            }
        } else {
            return "Vous devez être connecté à internet.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(nofail) {
            activity.onError(result);
        }else {
            activity.onSuccess(result);
        }
    }

}