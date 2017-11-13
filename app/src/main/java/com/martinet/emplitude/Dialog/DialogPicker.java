package com.martinet.emplitude.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Window;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Flo on 18/01/2016.
 */
public class DialogPicker extends DialogFragment{

    private static final String MOVE_IN_DATE_KEY = "monPicker";
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public static DialogPicker newInstance(Date date, DatePickerDialog.OnDateSetListener onDateSetListener) {
        DialogPicker pickerFragment = new DialogPicker();
        pickerFragment.setOnDateSetListener(onDateSetListener);

        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVE_IN_DATE_KEY, date);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }

    public static DialogPicker newInstance(Date date, DatePickerDialog.OnDateSetListener onDateSetListener, long dateFirst, long dateEnd) {
        DialogPicker pickerFragment = newInstance(date, onDateSetListener);
        pickerFragment.getArguments().putLong("dateFirst", dateFirst);
        pickerFragment.getArguments().putLong("dateEnd", dateEnd);
        return pickerFragment;
    }


    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Date initialDate = (Date) getArguments().getSerializable(MOVE_IN_DATE_KEY);
        long firstDate = getArguments().getLong("dateFirst");
        long endDate = getArguments().getLong("dateEnd");
        int[] yearMonthDay = ymdTripleFor(initialDate);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]);
        if(getArguments().containsKey("dateFirst")) {
            dialog.getDatePicker().setMinDate(firstDate);
            dialog.getDatePicker().setMaxDate(endDate);
        }
        dialog.setTitle("");
        return dialog;
    }

    private void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.onDateSetListener = listener;
    }

    private int[] ymdTripleFor(Date date) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)};
    }


}
