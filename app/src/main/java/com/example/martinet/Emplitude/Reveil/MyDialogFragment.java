package com.example.martinet.Emplitude.Reveil;

/**
 * Created by Arnaud on 04/01/2016.
 */
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

//Ce code est temporaire et sert retourner un timpiecker remplis pour l' afficher  quand on clique sur "programmer" et le faire fonctionner

public class MyDialogFragment extends DialogFragment {
    private int timeHour;
    private int timeMinute;
    private Handler handler;
    public MyDialogFragment(Handler handler){
        this.handler = handler;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        timeHour = bundle.getInt(MyConstants.HOUR);
        timeMinute = bundle.getInt(MyConstants.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeHour = hourOfDay;
                timeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(MyConstants.HOUR, timeHour);
                b.putInt(MyConstants.MINUTE, timeMinute);
                Message msg = new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        return new TimePickerDialog(getActivity(), listener, timeHour, timeMinute, false);
    }
}