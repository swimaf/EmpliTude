package com.martinet.emplitude.Todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.martinet.emplitude.Global;
import com.martinet.emplitude.Models.Task;
import com.martinet.emplitude.Tool.SwipeDismissListViewTouchListener;
import com.martinet.emplitude.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by florian on 13/11/15.
 */
public class Todo extends Fragment{

    public static Task activeTask;
    private ListView list;
    private FloatingActionButton action;
    private TextView aucune;
    private View view;
    public List<Task> tasks;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.getActivity().setTitle("TodoList");

        this.tasks = Global.global.getTasks();
        this.view = inflater.inflate(R.layout.todo, container, false);
        this.list = (ListView) view.findViewById(R.id.taches);
        this.action = (FloatingActionButton) view.findViewById(R.id.fab);
        this.aucune = (TextView) view.findViewById(R.id.aucune);

        /*if(!tasks.isEmpty()) {
            System.out.println(tasks.get(0).getMatiere()+" sdsd ");
        }*/
        System.out.println(tasks);

        this.creationListeTaches();

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ajouter.class);
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });




        return view;
    }

    public void creationListeTaches (){
        if (tasks.isEmpty()){
            aucune.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }else {
            Collections.sort(tasks, new Comparator<Object>() {
                public int compare(Object m1, Object m2) {
                    Date d = ((Task) m1).getDate();
                    Date d2 = ((Task) m2).getDate();
                    return d.compareTo(d2);
                }
            });

            final Adapter adapter = new Adapter(getActivity(), tasks);

            aucune.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            list.setAdapter(adapter);
            list.smoothScrollToPosition(2);
            list.setSelection(2);
            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(list,
                            new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                public boolean canDismiss(int position) {
                                    return true;
                                }

                                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                    Todo.activeTask = tasks.get(reverseSortedPositions[0]);
                                    tasks.remove(reverseSortedPositions[0]);
                                    creationListeTaches();
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Tâche supprimé avec succès", Snackbar.LENGTH_LONG)
                                            .setAction("ANNULER", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Snackbar snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Tâche restauré", Snackbar.LENGTH_SHORT);
                                                    tasks.add(Todo.activeTask);
                                                    creationListeTaches();
                                                    snackbar1.show();
                                                }
                                            }).show();
                                }
                            });
            list.setOnTouchListener(touchListener);
            list.setOnScrollListener(touchListener.makeScrollListener());



        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            this.creationListeTaches();
        }
    }

    public void modifierTache(int position, Task task){
        Bundle objetbunble = new Bundle();
        objetbunble.putSerializable("Task", task);
        Intent intent = new Intent(getContext(), Ajouter.class);
        intent.putExtras(objetbunble);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
    }

}