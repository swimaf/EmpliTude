package com.martinet.emplitude.Initialization;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Models.City;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Tool.Response;

import java.util.List;

/**
 * Created by martinet on 11/08/16.
 */

public class SelectionCity extends FragmentSelection implements Response<String, String> {

    private Spinner spnSelection;
    private ProgressBar prgLoading;
    private List<City> cities;
    private TextView tvNoFound;
    private TextView tvTitle;
    private City city;

    public static SelectionCity newInstance()
    {
        SelectionCity selectionCity = new SelectionCity();
        selectionCity.setArguments(new Bundle());
        return selectionCity;
    }

    public static SelectionCity newInstance(City city)
    {
        SelectionCity selectionCity = newInstance();
        selectionCity.getArguments().putSerializable("city", city);
        return selectionCity;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.city = (City) getArguments().get("city");

        View view = inflater.inflate(R.layout.initialisation_spinner, container, false);
        if(getActivity() instanceof Initialisation) {
            initialisation.getBtnPrevious().setVisibility(View.VISIBLE);
        } else {
            initialisation.getBtnPrevious().setVisibility(View.GONE);
        }
        initialisation.getBtnPrevious().setOnClickListener(onRevert());
        initialisation.getBtnNext().setOnClickListener(onValidate());

        tvTitle = (TextView) view.findViewById(R.id.titleSelection);
        spnSelection = (Spinner) view.findViewById(R.id.spnSelection);
        prgLoading = (ProgressBar) view.findViewById(R.id.loading);
        tvNoFound = (TextView) view.findViewById(R.id.nofound);

        tvTitle.setText(getResources().getText(R.string.selection_city_title));

        City.executeURL(this);

        return view;
    }


    public View.OnClickListener onValidate() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(cities == null) {
                    Snackbar.make(v, "Vous devez d'abord choisir une ville.", Snackbar.LENGTH_SHORT).show();
                } else {
                    SelectionSchool selection = SelectionSchool.newInstance(cities.get(cities.indexOf(spnSelection.getSelectedItem())));
                    Constants.changeFragment(getActivity(), R.id.selection, selection, R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        };
    }

    @Override
    public View.OnClickListener onRevert() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(getActivity() instanceof Initialisation) {
                    Bienvenue bienvenue = Bienvenue.newInstance();
                    Constants.changeFragment(initialisation.getFragmentActivity(), R.id.fragment, bienvenue, R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        };
    }

    @Override
    public void onSuccess(String value) {
        prgLoading.setVisibility(View.GONE);
        cities = City.parseJson(value);
        if(cities == null) {
            this.onError(getResources().getString(R.string.selectionCityNull));
        } else {
            spnSelection.setVisibility(View.VISIBLE);
            ArrayAdapter<City> adapter = new ArrayAdapter<>(initialisation.getFragmentActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
            spnSelection.setAdapter(adapter);
            if(city != null) {
                spnSelection.setSelection(adapter.getPosition(city));
            }
        }

    }

    @Override
    public void onError(String message) {
        prgLoading.setVisibility(View.GONE);
        tvNoFound.setText(message);
        ((LinearLayout) tvNoFound.getParent()).setVisibility(View.VISIBLE);
    }
}
