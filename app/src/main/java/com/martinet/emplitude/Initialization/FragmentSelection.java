package com.martinet.emplitude.Initialization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Group;
import com.martinet.emplitude.Tool.Fichier;

/**
 * Created by martinet on 12/09/16.
 */

public class FragmentSelection extends Fragment {

    public Creation initialisation;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initialisation = (Creation) getActivity();
    }

    public View.OnClickListener onRevert() {
        return null;
    }

    public View.OnClickListener onValidate() {
        return null;
    }

    public void addGroup(Group group) {
        if(!Global.global.getStudent().getGroups().contains(group)) {
            Global.global.getStudent().getGroups().add(group);
            Intent intent = new Intent(initialisation.getFragmentActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            intent.putExtra("openDrawer", true);
            startActivity(intent);
            initialisation.getFragmentActivity().finish();
            Global.global.getStudent().save();
        } else {
            Toast.makeText(initialisation.getFragmentActivity(), "Oops, vous avez déjà ajouté ce groupe !", Toast.LENGTH_SHORT).show();
        }
    }
}