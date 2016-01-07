package com.example.martinet.Emplitude.Reveil;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.martinet.Emplitude.R;


/**
 * Created by martinet on 13/11/15.
 */
public class Reveil extends Fragment {

    private Switch torch;
    private View view;
    private Camera camera;
    private Camera.Parameters p ;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        this.view           = inflater.inflate(R.layout.reveil, container, false);
        this.torch          = (Switch) view.findViewById(R.id.torch);
        this.camera         = Camera.open();
        this.p              = camera.getParameters();

        this.getActivity().setTitle("Réveil");

        this.torch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    camera.startPreview();
                } else {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    camera.stopPreview();
                }
            }
        });

        return view;
    }
}