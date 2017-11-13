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
import com.martinet.emplitude.Models.School;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Tool.Response;

import java.util.List;

/**
 * Created by martinet on 11/08/16.
 */

public class SelectionSchool extends FragmentSelection implements Response<String, String> {

    private City city;
    private Spinner spnSelection;
    private TextView tvNoFound;
    private TextView tvTitle;
    private ProgressBar prgLoading;
    private List<School> schools;
    private School school;

    public static SelectionSchool newInstance(City city)
    {
        SelectionSchool selectionSchool = new SelectionSchool();
        Bundle bdl = new Bundle();
        bdl.putSerializable("city", city);
        selectionSchool.setArguments(bdl);
        return selectionSchool;
    }

    public static SelectionSchool newInstance(City city, School school)
    {
        SelectionSchool selectionSchool = newInstance(city);
        selectionSchool.getArguments().putSerializable("school", school);
        return selectionSchool;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.school = (School) getArguments().get("school");
        this.city = (City) getArguments().get("city");

        View view = inflater.inflate(R.layout.initialisation_spinner, container, false);
        initialisation.getBtnNext().setOnClickListener(onValidate());
        initialisation.getBtnPrevious().setOnClickListener(onRevert());
        initialisation.getBtnPrevious().setVisibility(View.VISIBLE);

        tvTitle = (TextView) view.findViewById(R.id.titleSelection);
        spnSelection = (Spinner) view.findViewById(R.id.spnSelection);
        prgLoading = (ProgressBar) view.findViewById(R.id.loading);
        tvNoFound = (TextView) view.findViewById(R.id.nofound);

        tvTitle.setText(getResources().getText(R.string.selection_school_title));
        School.executeURL(this, city);

        return view;
    }


    public View.OnClickListener onValidate() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(schools == null) {
                    Snackbar.make(v, "Vous devez d'abord choisir un établissement.", Snackbar.LENGTH_SHORT).show();
                } else {
                    School school = schools.get(schools.indexOf(spnSelection.getSelectedItem()));
                    school.setCity(city);

                    SelectionGroup selectionGroup = SelectionGroup.newInstance(school);
                    Constants.changeFragment( initialisation.getFragmentActivity(), R.id.selection, selectionGroup, R.anim.slide_in_left, R.anim.slide_out_right);
                }

            }
        };
    }

    public View.OnClickListener onRevert() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                SelectionCity selection = SelectionCity.newInstance(city);
                Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };
    }

    @Override
    public void onSuccess(String value) {
        prgLoading.setVisibility(View.GONE);
        schools = School.parseJson(value);
        if(schools == null) {
            this.onError(getResources().getString(R.string.selectionSchoolNull));
        } else {
            spnSelection.setVisibility(View.VISIBLE);
            ArrayAdapter<School> adapter = new ArrayAdapter<>(initialisation.getFragmentActivity(), android.R.layout.simple_spinner_dropdown_item, schools);
            spnSelection.setAdapter(adapter);
            if(school != null) {
                spnSelection.setSelection(adapter.getPosition(school));
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
