package com.example.martinet.Emplitude.Emploi;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.martinet.Emplitude.MainActivity;
import com.example.martinet.Emplitude.R;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

public class ColorPicker extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_picker, container);
        Button annuler = (Button)view.findViewById(R.id.cancelButton);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final ColorPickerView mColorPickerView = (ColorPickerView) view.findViewById(R.id.colorpickerview__color_picker_view);
        Button ok = (Button) view.findViewById(R.id.okButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JourEmploi fragmentAppeler = (JourEmploi) ((Emploi) ((MainActivity) getActivity()).getFragment()).getFragment();
                fragmentAppeler.setColorButton(mColorPickerView.getColor());
                dismiss();
            }
        });
        getDialog().setTitle("Selectionner une couleur");

        return view;
    }
}