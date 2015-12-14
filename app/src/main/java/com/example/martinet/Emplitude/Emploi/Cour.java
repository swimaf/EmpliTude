package com.example.martinet.Emplitude.Emploi;

import android.content.Context;
import android.widget.Button;

/**
 * Created by martinet on 12/11/15.
 */
public class Cour extends Button {
    private int index;
    public Cour(int i, String titre, Context c){
        super(c);
        this.setText(titre);
        this.index = i;
    }
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int i){
        this.index = i;
    }
}
