package com.example.martinet.Emplitude.Outil;

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

    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    public static void ecrire(File f, Object... objects){
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(f)));
            for(Object object:objects){
                oos.writeObject(object);
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void ecrireVector(File f,Vector<Object> vector){
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(f)));
            for(int i=0; i<vector.size(); i++){
                oos.writeObject(vector.get(i));
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object lire(File f, int index){
        Object o = null;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(f)));
            for(int i=0; i<index+1; i++){
                o = ois.readObject();
            }
            ois.close();
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }
    public static Vector<Object> readAll(File f){
        Object o;
        Vector<Object> objects = new Vector<>();
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(f)));
            try{
                while(true) {
                    objects.add(ois.readObject());
                }
            }catch(EOFException e){
            }

            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }
}
