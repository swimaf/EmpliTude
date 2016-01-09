package com.example.martinet.Emplitude;

import java.io.File;

/**
 * Created by martinet on 09/01/16.
 */
public class Constants {
    final static public String store = System.getenv("EXTERNAL_STORAGE");
    final static public File courFile = new File(store+"/ADE.cours");
    final static public File identifiantFile = new File(store+"/.identifiant.txt");
}
