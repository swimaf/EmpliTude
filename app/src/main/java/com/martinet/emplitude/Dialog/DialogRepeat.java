package com.martinet.emplitude.Dialog;
/**
 * Created by Arnaud on 04/01/2016.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.martinet.emplitude.Alarm.ReveilActivity;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Schelude.Display.ActivitySchelude;
import com.martinet.emplitude.Schelude.FragmentLesson;

import java.util.Date;


public class DialogRepeat extends DialogFragment {

    public static DialogRepeat getInstance(int preferenceRepeat) {
        DialogRepeat fragment = new DialogRepeat();
        Bundle args = new Bundle();
        args.putInt("repeat", preferenceRepeat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choisir le nombre de répétition de sonnerie");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialog_repeter, null);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.nprFoisRepeter);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(getArguments().getInt("repeat", 1));
        numberPicker.setWrapSelectorWheel(true);

        builder.setView(view)
                .setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((ReveilActivity)((MainActivity)getActivity()).getFragment()).editRepeat(numberPicker.getValue());
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}