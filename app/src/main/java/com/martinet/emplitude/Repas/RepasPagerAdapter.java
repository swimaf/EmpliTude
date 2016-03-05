package com.martinet.emplitude.Repas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by martinet on 05/03/16.
 */

public class RepasPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 5;
    private String joursSemaines[];
    private Context context;

    public RepasPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        joursSemaines = new String[7];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        DateFormat df = new SimpleDateFormat("EEE dd MMM", Locale.FRANCE);
        for (int i = 0; i < 5; i++) {
            joursSemaines[i] = df.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);

        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return RepasPage.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return joursSemaines[position];
    }
}
