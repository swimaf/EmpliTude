package com.example.martinet.Emplitude.Todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TimePicker;

import com.example.martinet.Emplitude.Reveil.MyConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Flo on 18/01/2016.
 */
public class DialogDate extends DialogFragment{

    private OnDateSetListener onDateSetListener;

    static DialogDate newInstance(Date date, OnDateSetListener onDateSetListener) {
        DialogDate pickerFragment = new DatePickerFragment();
        pickerFragment.setOnDateSetListener(onDateSetListener);

        //Pass the date in a bundle.
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVE_IN_DATE_KEY, date);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Date initialDate = (Date) getArguments().getSerializable(MOVE_IN_DATE_KEY);
        int[] yearMonthDay = ymdTripleFor(initialDate);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, yearMonthDay[0], yearMonthDay[1],
                yearMonthDay[2]);
        return dialog;
    }

    private void setOnDateSetListener(OnDateSetListener listener) {
        this.onDateSetListener = listener;
    }

    private int[] ymdTripleFor(Date date) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)};
    }
}
