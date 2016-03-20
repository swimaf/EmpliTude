package com.martinet.emplitude;

import android.app.Application;

import com.martinet.emplitude.Outil.Fichier;
import com.martinet.emplitude.Todo.Tache;
import com.martinet.emplitude.Todo.Todo;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

/**
 * Classe d'initialisation de l'application
 */

public class MyApplication extends Application {

    public Vector<Object> mesTaches;
    @Override
    public void onCreate()
    {
        super.onCreate();
        //Permet de récupérer une fois les tâches
        this.mesTaches = Fichier.readAll(Constants.TACHE_FILE, getBaseContext());
    }

    /*Methode de trie des taches en fonction de la date*/
    public void trierTache(){
        Collections.sort(mesTaches, new Comparator<Object>() {
            public int compare(Object m1, Object m2) {
                Date d = ((Tache) m1).getDate();
                Date d2 = ((Tache) m2).getDate();
                return d.compareTo(d2);
            }
        });
    }


}
