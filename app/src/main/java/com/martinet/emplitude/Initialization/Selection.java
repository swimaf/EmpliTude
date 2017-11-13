package com.martinet.emplitude.Initialization;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.R;

/**
 * Created by martinet on 10/08/16.
 */

public class Selection extends FragmentSelection {

    public static Selection newInstance()
    {
        Selection selection = new Selection();
        selection.setArguments(new Bundle());
        return selection;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initFragment(SelectionCity.newInstance());
        return inflater.inflate(R.layout.initialisation_selection, container, false);
    }

    private void initFragment(Fragment fragment) {
        Constants.changeFragment(getActivity(), R.id.selection, fragment);
    }

}
