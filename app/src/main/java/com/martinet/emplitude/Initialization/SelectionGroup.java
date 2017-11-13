package com.martinet.emplitude.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Group;
import com.martinet.emplitude.Models.School;
import com.martinet.emplitude.Models.Student;
import com.martinet.emplitude.Tool.Response;
import com.martinet.emplitude.R;

import java.util.List;

/**
 * Created by martinet on 11/08/16.
 */

public class SelectionGroup extends FragmentSelection implements Response<String, String> {

    private School school;
    private Spinner spnSelection;
    private TextView tvNoFound;
    private TextView tvTitle;
    private ProgressBar prgLoading;
    private List<Group> groups;

    public static SelectionGroup newInstance()
    {
        SelectionGroup selectionGroup = new SelectionGroup();
        Bundle bdl = new Bundle();
        selectionGroup.setArguments(bdl);
        return selectionGroup;
    }

    public static SelectionGroup newInstance(School school)
    {
        SelectionGroup selectionGroup = newInstance();
        selectionGroup.getArguments().putSerializable("school", school);
        return selectionGroup;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.school = (School) getArguments().get("school");

        View view = inflater.inflate(R.layout.initialisation_spinner, container, false);
        initialisation.getBtnNext().setOnClickListener(onValidate());
        initialisation.getBtnPrevious().setOnClickListener(onRevert());
        initialisation.getBtnNext().setText(R.string.next);
        initialisation.getBtnNext().setVisibility(View.VISIBLE);

        tvTitle = (TextView) view.findViewById(R.id.titleSelection);
        spnSelection = (Spinner) view.findViewById(R.id.spnSelection);
        prgLoading = (ProgressBar) view.findViewById(R.id.loading);
        tvNoFound = (TextView) view.findViewById(R.id.nofound);

        tvTitle.setText(getResources().getText(R.string.selection_group_title));
        Group.executeURL(this, school);

        return view;
    }


    public View.OnClickListener onValidate() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if(groups == null) {
                    Snackbar.make(v, "Vous devez d'abord choisir un groupe.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Fragment selection;
                    Group group = (Group) spnSelection.getSelectedItem();
                    group.setSchool(school);
                    if(group.getAuthentification()) {
                        selection = SelectionAuthentification.newInstance(group);
                    } else {
                        if(Global.global.getStudent() == null) {
                            Student student = new Student(group);
                            selection = SelectionLoading.newInstance(student);
                        } else {
                            addGroup(group);
                            return;
                        }
                    }
                    Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        };
    }

    public View.OnClickListener onRevert() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                SelectionSchool selection = SelectionSchool.newInstance(school.getCity(), school);
                Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };
    }

    @Override
    public void onSuccess(String value) {
        prgLoading.setVisibility(View.GONE);
        groups = Group.parseJson(value);
        if(groups == null) {
            this.onError(initialisation.getFragmentActivity().getResources().getString(R.string.selectionGroupNull));
        } else {
            spnSelection.setVisibility(View.VISIBLE);

            ArrayAdapter<Group> adapter = new ArrayAdapter<>(initialisation.getFragmentActivity(), android.R.layout.simple_spinner_dropdown_item, groups);
            spnSelection.setAdapter(adapter);
        }

    }

    @Override
    public void onError(String message) {
        prgLoading.setVisibility(View.GONE);
        tvNoFound.setText(message);
        ((LinearLayout) tvNoFound.getParent()).setVisibility(View.VISIBLE);
    }
}
