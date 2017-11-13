package com.martinet.emplitude.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.Loading;
import com.martinet.emplitude.Models.Student;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Schelude.Schedule;
import com.martinet.emplitude.Tool.Fichier;
import com.martinet.emplitude.Tool.Response;

/**
 * Created by martinet on 11/08/16.
 */

public class SelectionLoading extends FragmentSelection implements Response<String, String> {

    private Student student;
    private TextView tvError;
    private LinearLayout llVisible;

    public static SelectionLoading newInstance(Student student)
    {
        SelectionLoading selectionLoading = new SelectionLoading();
        Bundle bdl = new Bundle();
        bdl.putSerializable("student", student);
        selectionLoading.setArguments(bdl);
        return selectionLoading;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.student = (Student) getArguments().get("student");

        View view = inflater.inflate(R.layout.initialisation_loading, container, false);
        initialisation.getBtnNext().setOnClickListener(onValidate());
        initialisation.getBtnPrevious().setOnClickListener(onRevert());
        initialisation.getBtnNext().setVisibility(View.GONE);
        tvError = (TextView) view.findViewById(R.id.error);
        llVisible = (LinearLayout) view.findViewById(R.id.onLoading);
        student.save();
        Global.global.setStudent(student);
        new Schedule().load(this, student);
        return view;
    }


    public View.OnClickListener onValidate() {return null;}

    public View.OnClickListener onRevert() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Fragment selection;
                if(student.getDefaultGroup().getAuthentification()) {
                    selection = SelectionAuthentification.newInstance(student.getDefaultGroup());
                } else {
                    selection = SelectionGroup.newInstance(student.getDefaultGroup().getSchool());
                }
                Constants.changeFragment(initialisation.getFragmentActivity(), R.id.selection, selection, R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };
    }

    public void onSuccess(String value) {
        Intent intent = new Intent(getActivity(), Loading.class);
        this.startActivity(intent);
        initialisation.getFragmentActivity().finish();
    }


    public void onError(String message) {
        tvError.setText(message);
        student.delete();
        ((LinearLayout) tvError.getParent()).setVisibility(View.VISIBLE);
        llVisible.setVisibility(View.GONE);
    }
}
