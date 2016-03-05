package com.martinet.emplitude.Repas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinet.emplitude.R;

/**
 * Created by martinet on 05/03/16.
 */

public class RepasPage extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static RepasPage newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RepasPage fragment = new RepasPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repas_page, container, false);
        return view;
    }
}