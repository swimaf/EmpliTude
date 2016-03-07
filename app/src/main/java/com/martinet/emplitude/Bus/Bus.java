package com.martinet.emplitude.Bus;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinet on 05/03/16.
 */

public class Bus extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("Bus");

        View view = inflater.inflate(R.layout.bus, container, false);

        final Spinner sDepart = (Spinner) view.findViewById(R.id.sDepart);
        final Spinner sArret = (Spinner) view.findViewById(R.id.sArret);
        final LinearLayout resultats = (LinearLayout) view.findViewById(R.id.resultat);
        String[] list = getResources().getStringArray(R.array.arret);
        ArrayAdapter<String> departement = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        sDepart.setAdapter(departement);

        sDepart.setSelection(1);
        sArret.setAdapter(departement);

        ImageButton change = (ImageButton) view.findViewById(R.id.changer);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexDepart = sDepart.getSelectedItemPosition();
                int indexArret = sArret.getSelectedItemPosition();

                sDepart.setSelection(indexArret);
                sArret.setSelection(indexDepart);
            }
        });

        ImageButton search = (ImageButton) view.findViewById(R.id.rechercher);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultats.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

}