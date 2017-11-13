package com.martinet.emplitude.Initialization;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.R;


/**
 * Created by martinet on 16/11/15.
 */
public class Bienvenue extends FragmentSelection {

    public static Bienvenue newInstance()
    {
        Bienvenue bienvenue = new Bienvenue();
        Bundle bdl = new Bundle();
        bienvenue.setArguments(bdl);
        return bienvenue;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.initialisation = (Initialisation) getActivity();
        initialisation.getBtnPrevious().setVisibility(View.GONE);
        initialisation.getBtnNext().setOnClickListener(onValidate());
        return inflater.inflate(R.layout.initialisation_welcome, container, false);
    }

    public View.OnClickListener onValidate() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Selection selection = Selection.newInstance();
                Constants.changeFragment(getActivity(), R.id.fragment, selection, R.anim.slide_in_left, R.anim.slide_out_right);
            }
        };
    }
}