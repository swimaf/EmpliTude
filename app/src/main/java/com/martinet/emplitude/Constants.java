package com.martinet.emplitude;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;


public class Constants {
    final static public String store = System.getenv("EXTERNAL_STORAGE");
    final static public String courFile ="ADE.cours";
    final static public String identifiantFile = ".identifiant.txt";
    final static public String PREFERENCE_SON = "PREFERENCE_SON";
    final static public String PREFERENCE_ADE = "Ade";
    final static public String PREFERENCE_COULEUR = "Couleur";


    final static public int intervaleSonnerieRepeter = 1;

    final static public String tacheFile = "tache.txt";


    public static Boolean CONNECTED(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static int getHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if (width > height) {
            height = width - 150;
        } else {
            height = height - 150;
        }
        return height;
    }

    public static int getBrightness(int color) {
        return (int) Math.sqrt(Color.red(color) * Color.red(color) * .241 +
                Color.green(color) * Color.green(color) * .691 +
                Color.blue(color) * Color.blue(color) * .068);
    }

    public static int getColorWB(int color) {
        if (getBrightness(color) < 130) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }
}
