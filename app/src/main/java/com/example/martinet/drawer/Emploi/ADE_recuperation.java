package com.example.martinet.drawer.Emploi;

import android.os.AsyncTask;

import com.example.martinet.drawer.Fichier;
import com.example.martinet.drawer.Utilisateur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ADE_recuperation extends AsyncTask<Void, Void, Void> {

    final private String store = System.getenv("EXTERNAL_STORAGE") ;
    final private File file = new File(this.store+"/.identifiant.txt");

    private Utilisateur utilisateur;
    private String textResult;
    private String source;
    private loadFichier o;

    public ADE_recuperation(loadFichier o){
        Jour j = new Jour(new Date());
        String first = j.getUrl();
        j.ajouterJour(14);
        String last =j.getUrl();
        j.ajouterJour(-5);
        this.o = o;
        this.utilisateur = (Utilisateur) Fichier.lire(file, 0);
        this.source = "https://planning.univ-rennes1.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources="+utilisateur.getIdentifiant()+"&projectId=1&calType=ical&firstDate="+first+"&lastDate="+last;

    }

    protected Void doInBackground(Void... params) {
        URL textUrl;

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {

        } catch (Exception e) {
            System.out.println("Erreur SSL");
        }


        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            textUrl = new URL(source);
            URLConnection connection = textUrl.openConnection();
            InputStream is = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            textResult = sb.toString();
            File file = new File(store+"/c.ical");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textResult);
            bw.close();


        } catch(Exception e) {
            e.printStackTrace();
            textResult = e.toString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        o.retour("Mise à jour effectué");
    }

}