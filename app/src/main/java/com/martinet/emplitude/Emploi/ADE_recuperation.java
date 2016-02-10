package com.martinet.emplitude.Emploi;

import android.content.Context;
import android.os.AsyncTask;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Outil.Jour;
import com.martinet.emplitude.Outil.Utilisateur;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ADE_recuperation extends AsyncTask<Void, Void, Void> {

    public final static int NO_ERREUR = 0;
    public final static int ERROR_INTERNET = 1;
    public final static int ERROR_SSL = 2;
    public final static int ERROR_ADE = 3;
    public final static int ERROR = 4;
    public static String INFO = "";

    private Utilisateur utilisateur;
    private String textResult;
    private String source;
    private ADE_retour o;
    private int retour;
    private Context context;

    public ADE_recuperation(ADE_retour o, Context c){
        Jour j = new Jour(new Date());
        String first = j.getUrl();
        j.ajouterJour(14);
        String last =j.getUrl();
        this.o = o;
        this.context =c;
        this.utilisateur = (Utilisateur) Fichier.lire(Constants.identifiantFile,context, 0);
        this.source = "https://planning.univ-rennes1.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources="+utilisateur.getIdentifiant()+"&projectId=1&calType=ical&firstDate="+first+"&lastDate="+last;
    }

    protected Void doInBackground(Void... params) {
        URL textUrl;
        retour = ERROR;
        INFO = "La mise à jour a échouer.";
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
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            textUrl = new URL(source);
            URLConnection connection = textUrl.openConnection();
            InputStream is = connection.getInputStream();
            retour = ERROR_SSL;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }

            textResult = sb.toString();
            Vector cours = ADE_traitement.get(textResult);
            Fichier.ecrireVector(Constants.courFile, context, cours);


        } catch(Exception e) {
            e.printStackTrace();
            textResult = e.toString();
            retour = ERROR_ADE;
        }
        retour = NO_ERREUR;
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        INFO = "Mise à jour effectuée";
        o.retour(retour);
    }

}
