package com.martinet.emplitude.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Student;
import com.martinet.emplitude.Models.Group;
import com.martinet.emplitude.R;

/**
 * Created by martinet on 11/08/16.
 */

public class SelectionAuthentification extends FragmentSelection {

    private Group group;
    private TextView tvPseudo;
    private TextView tvPassword;

    public static SelectionAuthentification newInstance()
    {
        SelectionAuthentification selectionAuthentification = new SelectionAuthentification();
        Bundle bdl = new Bundle();
        selectionAuthentification.setArguments(bdl);
        return selectionAuthentification;
    }

    public static SelectionAuthentification newInstance(Group group)
    {
        SelectionAuthentification selectionAuthentification = newInstance();
        selectionAuthentification.getArguments().putSerializable("group", group);
        return selectionAuthentification;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.group = (Group) getArguments().get("group");

        View view = inflater.inflate(R.layout.initialisation_authentification, container, false);
        initialisation.getBtnNext().setOnClickListener(onValidate());
        initialisation.getBtnPrevious().setOnClickListener(onRevert());
        initialisation.getBtnNext().setText(R.string.finish);
        initialisation.getBtnNext().setVisibility(View.VISIBLE);

        tvPseudo = (TextView) view.findViewById(R.id.tvPseudo);
        tvPassword = (TextView) view.findViewById(R.id.tvPassword);
        return view;
    }


    public View.OnClickListener onValidate() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                if("".equals(tvPseudo.getText().toString().trim())) {
                    tvPseudo.setError("Le champ est requis");
                    return;
                }
                if("".equals(tvPassword.getText().toString().trim())) {
                    tvPassword.setError("Le champ est requis");
                    return;
                }
                group.setPseudo(tvPseudo.getText().toString());
                group.setPassword(tvPassword.getText().toString());
                if(Global.global.getStudent() == null) {
                    Student student = new Student(group);
                    SelectionLoading selection = SelectionLoading.newInstance(student);
                    Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    addGroup(group);
                }

            }
        };
    }

    public View.OnClickListener onRevert() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                SelectionGroup selection = SelectionGroup.newInstance(group.getSchool());
                Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };
    }

}
