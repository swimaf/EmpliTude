package com.example.martinet.Emplitude.Son;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.martinet.Emplitude.R;


/**
 * Created by martinet on 13/11/15.
 */
public class Son extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("Gestion des sons");
        return inflater.inflate(R.layout.sons, container, false);
    }
}
