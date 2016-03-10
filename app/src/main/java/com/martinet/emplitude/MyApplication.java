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
 * Created by Flo on 09/03/2016.
 */
public class MyApplication extends Application {

    public Vector<Object> mesTaches;
    @Override
    public void onCreate()
    {
        super.onCreate();
        this.mesTaches = Fichier.readAll(Constants.tacheFile, getBaseContext());
    }

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
