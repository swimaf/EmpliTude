package com.martinet.emplitude.Todo;
/**
 * Created by Flo on 21/12/15.
 */
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.MyApplication;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * Classe permettant de gérer l'affichage de la liste de tâches
 */
public class Adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final Vector<Object> lesTaches;
    private static SimpleDateFormat h = new SimpleDateFormat("EEEE dd MMMM", Locale.FRANCE);
    private MyApplication application;
    public Adapter(Activity context, Vector taches) {
        super(context, R.layout.todo_tache, taches);
        this.context = context;
        this.lesTaches = taches;
        this.application = (MyApplication) context.getApplicationContext();
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
        Date jour = new Date();
        Tache t;


        try {
            t = (Tache) lesTaches.get(position);
            tache.setText(t.getNom());
            if(t.getCours() == null){
                matiere.setText(" Aucune");
            }else{
                matiere.setText(" "+t.getCours().getMatiere());
            }
            String d = h.format(t.getDate());
            d = d.substring(0, 1).toUpperCase() + d.substring(1);
            date.setText(" "+d);
            if(jour.after(t.getDate())){
                rowView.findViewById(R.id.tacheLayout).setBackgroundResource(R.color.tacheGris);
            }
        }catch (Exception e){}




        modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo t=(Todo)((MainActivity)context).getFragment();
                t.modifierTache(position, (Tache) lesTaches.get(position));
            }
        });

        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Todo t=(Todo)((MainActivity)context).getFragment();
                Todo.activeTache = (Tache)application.mesTaches.get(position);
                application.mesTaches.remove(position);
                t.creationListeTaches();
                Snackbar.make(context.findViewById(android.R.id.content), "Tâche supprimé avec succès", Snackbar.LENGTH_LONG)
                        .setAction("ANNULER", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(context.findViewById(android.R.id.content), "Tâche restauré", Snackbar.LENGTH_SHORT);
                                application.mesTaches.add(Todo.activeTache);
                                t.creationListeTaches();
                                snackbar1.show();
                            }
                        }).show();
            }
        });
        return rowView;
    }
    
}
