package com.martinet.emplitude.Tool;

import android.content.Context;
import android.util.Log;

import com.martinet.emplitude.Models.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 17/11/15.
 */

/**
 * Classe permettant la sauvargarde ou la récuperation de données
 */

public class Fichier<T> {

    public static void write(String file, Context c, Object... objects){
        try {
            FileOutputStream fos = c.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(Object object:objects){
                oos.writeObject(object);
            }
            oos.close();
            fos.close();
        } catch (IOException e) {}
    }
    public void write(String file, List<T> list, Context c){
        try {
            FileOutputStream fos = c.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(T object : list){
                oos.writeObject(object);
            }
            oos.close();
            fos.close();
        } catch (IOException ignored) {
            Log.e("Fichier", "IOException");
        }
    }

    public static Boolean exist(String file, Context c) {
        File f = c.getFileStreamPath(file);
        return f.exists();
    }

    public static Boolean delete(String file, Context c) {
        File f = c.getFileStreamPath(file);
        return f.delete();
    }

    public static Object readObject(String file, Context c, int index){
        Object o = null;
        try {
            FileInputStream fis = c.openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            for(int i=0; i<index+1; i++){
                o = ois.readObject();
            }
            ois.close();
            fis.close();
        } catch (IOException|ClassNotFoundException e) {}

        return o;
    }
    public List<T> readObjects(String file, Context c){
        List<T> objects = new ArrayList<>();
        try {
            FileInputStream fis = c.openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(true) {
                objects.add((T) ois.readObject());
            }
        } catch (Exception ignored) {}

        return objects.isEmpty() ? null : objects;
    }
}
