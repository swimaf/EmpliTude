package com.martinet.emplitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;


public class Constants {

    final static public String API_URL = "http://emplitude.info/api";

    public static class Files {
        final public static String STUDENT = ".student.txt";
        final static public String TASK = ".task.txt";
        final static public String LESSONS =".lessons.cours";
    }


    public static class Preference {
        final public static String SCHELURE = "PREFERENCE_SCHELURE";
        final public static String ALARM = "PREFERENCE_ALARM";
        final static public String SOUND = "PREFERENCE_SOUND";

    }

    public static Boolean CONNECTED() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Global.global);
        if(preferences.getBoolean("preference_wifi", false)) {
            WifiManager cm =(WifiManager) Global.global.getSystemService(Context.WIFI_SERVICE);
            return cm.isWifiEnabled();
        } else {
            ConnectivityManager cm =(ConnectivityManager) Global.global.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
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

    public static int getWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        return width;
    }

    public static void changeFragment(FragmentActivity context, int id, Fragment fragment){
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commit();
    }
    public static void changeFragment(FragmentActivity context, int id, Fragment fragment, int animIn, int animOut){
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(animIn, animOut);
        transaction.replace(id, fragment);
        transaction.commit();
    }

    public interface Predicate<T> { boolean apply(T type); }

    public static <T> List<T> filter(List<T> col, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T element: col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
