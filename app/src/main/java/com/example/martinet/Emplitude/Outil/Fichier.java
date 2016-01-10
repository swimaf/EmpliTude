package com.example.martinet.Emplitude.Outil;

import android.content.Context;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 * Created by martinet on 17/11/15.
 */

public class Fichier {

    public static void ecrire(String file, Context c, Object... objects){
        try {
            FileOutputStream fos = c.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(Object object:objects){
                oos.writeObject(object);
            }
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void ecrireVector(String file,Context c, Vector<Object> vector){
        try {
            FileOutputStream fos = c.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(int i=0; i<vector.size(); i++){
                oos.writeObject(vector.get(i));
            }
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean existe(String file, Context c) {
        File f = c.getFileStreamPath(file);
        return f.exists();
    }

    public static Boolean delete(String file, Context c) {
        File f = c.getFileStreamPath(file);
        return f.delete();
    }

    public static Object lire(String file, Context c, int index){
        Object o = null;
        try {
            FileInputStream fis = c.openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            for(int i=0; i<index+1; i++){
                o = ois.readObject();
            }
            ois.close();
            fis.close();
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }
    public static Vector<Object> readAll(String file, Context c){
        Vector<Object> objects = new Vector<>();
        try {
            FileInputStream fis = c.openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            try{
                while(true) {
                    objects.add(ois.readObject());
                }
            }catch(EOFException e){
            }

            ois.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }
}
