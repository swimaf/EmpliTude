package com.example.martinet.Emplitude.Todo;

/**
 * Created by florian on 21/12/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.martinet.Emplitude.Emploi.Jour;
import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.R;

import java.util.Collection;
import java.util.Vector;

public class Adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final Vector<Object> lesTaches;

    public Adapter(Activity context, Vector taches) {
        super(context, R.layout.todo_tache, taches);
        this.context = context;
        this.lesTaches = taches;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.todo_tache, null, true);
        TextView tache = (TextView) rowView.findViewById(R.id.tache);
        TextView matiere = (TextView) rowView.findViewById(R.id.matiere);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        ImageButton modif = (ImageButton) rowView.findViewById(R.id.modifier);
        ImageButton supp = (ImageButton) rowView.findViewById(R.id.supprimer);
        final Tache t;

        try {
            t = (Tache) lesTaches.get(position);
            tache.setText(t.getNom());
            matiere.setText("Matière : " + t.getMatiere());
            Jour j = new Jour(t.getDate());
            date.setText("Date : " + j.getJour());
        }catch (Exception e){

        }

        modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle objetbunble = new Bundle();
                objetbunble.putSerializable("Tache", (Tache) lesTaches.get(position));
                Intent intent = new Intent(getContext(), Ajouter.class);
                intent.putExtras(objetbunble);
                intent.putExtra("position",position);
                context.startActivityForResult(intent, 1);
                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo t=(Todo)((MainActivity)context).getFragment();
                Todo.mesTaches.remove(position);
                t.creationListeTaches();
            }
        });


        return rowView;
    }

    public void deleteTache (int id){

    }
}
