package com.example.martinet.Emplitude.Todo;

/**
 * Created by florian on 21/12/15.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.martinet.Emplitude.R;

public class Adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;

    public Adapter(Activity context, String[] web) {
        super(context, R.layout.todo_tache, web);
        this.context = context;
        this.web = web;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.todo_tache, null, true);
        TextView tache = (TextView) rowView.findViewById(R.id.tache);

        tache.setText(web[position]);

        return rowView;
    }
}
