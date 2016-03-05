package com.martinet.emplitude.Repas;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;

/**
 * Created by martinet on 05/03/16.
 */

public class Repas extends Fragment {

    private View toolbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("Repas");

        View view = inflater.inflate(R.layout.repas, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new RepasPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        LayoutInflater vi = LayoutInflater.from(getContext());
        View v = vi.inflate(R.layout.repas_toolbar, null);
        this.toolbar =v;
        tool.addView(v);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void onDestroyView(){
        super.onDestroyView();
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        tool.removeView(toolbar);
    }


}