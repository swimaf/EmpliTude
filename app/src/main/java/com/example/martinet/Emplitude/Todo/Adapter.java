package com.example.martinet.Emplitude.Todo;

/**
 * Created by florian on 21/12/15.
 */
import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
                Todo t=(Todo)((MainActivity)context).getFragment();
                Todo.mesTaches.remove(position);
                t.creationListeTaches();
            }
        });

        try {
            Tache t = (Tache) lesTaches.get(position);
            tache.setText(t.getNom());
            matiere.setText("Matière : " + t.getMatiere());
            date.setText("Date : " + t.getDate());
        }catch (Exception e){

        }
        return rowView;
    }

    public void deleteTache (int id){

    }
}
