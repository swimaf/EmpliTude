package com.example.martinet.Emplitude.Reveil;

/**
 * Created by root on 06/02/16.
 */
import java.io.File;
import java.io.FilenameFilter;

class MyMP3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}