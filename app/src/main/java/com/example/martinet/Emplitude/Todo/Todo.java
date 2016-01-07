package com.example.martinet.Emplitude.Todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.martinet.Emplitude.Emploi.Information;
import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by florian on 13/11/15.
 */
public class Todo extends Fragment{

    private ListView list;
    private FloatingActionButton action;
    String[] web = {
            "Faire a manger",
            "Faire ex2 ",
            "Aller faire les course",
            "Rien foutre",
            "Je sais pas",
            "Projet de ouff",
            "Voila"
    } ;

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("TodoList");

        this.view = inflater.inflate(R.layout.todo, container, false);
        this.list = (ListView) view.findViewById(R.id.taches);
        this.action = (FloatingActionButton) view.findViewById(R.id.fab);
        Adapter adapter = new Adapter(getActivity(), web);

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
}