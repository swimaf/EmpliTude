package com.example.martinet.Emplitude.Todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martinet.Emplitude.Constants;
import com.example.martinet.Emplitude.Outil.Fichier;
import com.example.martinet.Emplitude.R;

import java.util.Date;
import java.util.Vector;

/**
 * Created by florian on 13/11/15.
 */
public class Todo extends Fragment{

    public static Vector<Object> mesTaches;

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

        mesTaches = Fichier.readAll(Constants.tacheFile, getContext());

        this.creationListeTaches();

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ajouter.class);
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        return view;
    }

    public void creationListeTaches (){
        if (mesTaches.size() == 0){
            aucune.setVisibility(View.VISIBLE);
        }else {
            Adapter adapter = new Adapter(getActivity(), mesTaches);
            aucune.setVisibility(View.GONE);
            list.setAdapter(adapter);
            list.smoothScrollToPosition(2);
            list.setSelection(2);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            this.creationListeTaches();
            System.out.println("truc dedans");
        }
        if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        Fichier.ecrireVector(Constants.tacheFile, getContext(),mesTaches);
    }

}