package com.example.martinet.Emplitude.Todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.R;

import java.util.Date;
import java.util.Vector;

/**
 * Created by florian on 13/11/15.
 */
public class Todo extends Fragment{

    private ListView list;
    private FloatingActionButton action;
    private Vector<Object> listeTache;
    private Vector<Object> test;
    private Tache tache1;
    private Tache tache2;

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("TodoList");

        this.view = inflater.inflate(R.layout.todo, container, false);
        this.list = (ListView) view.findViewById(R.id.taches);
        this.action = (FloatingActionButton) view.findViewById(R.id.fab);

        this.listeTache = Fichier.readAll(Constants.tacheFile, getContext());
        Adapter adapter = new Adapter(getActivity(), listeTache);

        list.setAdapter(adapter);
        list.smoothScrollToPosition(2);
        list.setSelection(2);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ajouter.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        return view;
    }

    public void rafraichir(Vector listeTache){
        Adapter adapter = new Adapter(getActivity(), listeTache);

        list.setAdapter(adapter);
        list.smoothScrollToPosition(2);
        list.setSelection(2);
    }

}