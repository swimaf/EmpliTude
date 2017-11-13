package com.martinet.emplitude.ManageGroup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Group;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageGroupAdapter extends ArrayAdapter<Group>{

    private final Activity context;
    private List<Group> groups;

    public ManageGroupAdapter(Activity context, List<Group> groups) {
        super(context, R.layout.todo_tache, groups);
        this.groups = groups;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.manage_group_item, null, true);
        final Group group = groups.get(position);
        ((TextView) rowView.findViewById(R.id.tvName)).setText(group.getName());
        ((TextView) rowView.findViewById(R.id.tvCity)).setText(group.getSchool().getCity().getName());
        ((TextView) rowView.findViewById(R.id.tvSchool)).setText(group.getSchool().getName());
        final View constraintLayout = context.findViewById(R.id.rlBase);
        rowView.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.global.getStudent().getDefaultGroup().equals(group)) {
                    Snackbar.make(constraintLayout, "Vous ne pouvez pas supprimer le groupe par défault.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Global.global.getStudent().getGroups().remove(group);
                    ManageGroupAdapter.this.notifyDataSetChanged();
                    Global.global.getStudent().save();
                }
            }
        });
        return rowView;
    }

}
