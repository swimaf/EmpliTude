package com.example.martinet.Emplitude.Todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Emploi.Cour;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

/**
 * Created by florian on 13/11/15.
 */
public class Todo extends Fragment{

    public static Vector<Object> mesTaches;
    public static Tache activeTache;
    private ListView list;
    private FloatingActionButton action;
    private TextView aucune;
    private View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("TodoList");

        this.view = inflater.inflate(R.layout.todo, container, false);
        this.list = (ListView) view.findViewById(R.id.taches);
        this.action = (FloatingActionButton) view.findViewById(R.id.fab);
        this.aucune = (TextView) view.findViewById(R.id.aucune);

        this.creationListeTaches();

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ajouter.class);
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });




        return view;
    }

    public void creationListeTaches (){
        if (mesTaches.size() == 0){
            aucune.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }else {
            Collections.sort(mesTaches, new Comparator<Object>() {
                public int compare(Object m1, Object m2) {
                    Date d = ((Tache) m1).getDate();
                    Date d2 = ((Tache) m2).getDate();
                    return d.compareTo(d2);
                }
            });


            final Adapter adapter = new Adapter(getActivity(), mesTaches);


            aucune.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            list.setAdapter(adapter);
            list.smoothScrollToPosition(2);
            list.setSelection(2);
            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(list,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    Todo.activeTache = (Tache)Todo.mesTaches.get(reverseSortedPositions[0]);
                                    Todo.mesTaches.remove(reverseSortedPositions[0]);
                                    creationListeTaches();
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Tâche supprimé avec succès", Snackbar.LENGTH_LONG)
                                            .setAction("ANNULER", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Snackbar snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Tâche restauré", Snackbar.LENGTH_SHORT);
                                                    Todo.mesTaches.add(Todo.activeTache);
                                                    creationListeTaches();
                                                    snackbar1.show();
                                                }
                                            }).show();
                                }
                            });
            list.setOnTouchListener(touchListener);
            list.setOnScrollListener(touchListener.makeScrollListener());



        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            this.creationListeTaches();
        }
    }

    public void onStop(){
        Fichier.ecrireVector(Constants.tacheFile, getContext(),Todo.mesTaches);
        super.onStop();
    }

    public void modifierTache(int position, Tache tache){
        Bundle objetbunble = new Bundle();
        objetbunble.putSerializable("Tache", tache);
        Intent intent = new Intent(getContext(), Ajouter.class);
        intent.putExtras(objetbunble);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
    }

}