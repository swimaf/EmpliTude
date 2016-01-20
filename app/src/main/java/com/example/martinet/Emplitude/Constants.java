package com.example.martinet.Emplitude;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Constants {
    final static public String store = System.getenv("EXTERNAL_STORAGE");
    final static public String courFile ="ADE.cours";
    final static public String identifiantFile = ".identifiant.txt";
<<<<<<< HEAD
    final static public int intervaleSonnerieRepeter = 1;
=======
    final static public String tacheFile = "tache.txt";
>>>>>>> 30f546520b816cfbbaecc455360cf12ca20c25f1

    public static Boolean CONNECTED(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
