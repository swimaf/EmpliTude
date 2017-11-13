package com.martinet.emplitude.Schelude.Display;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.martinet.emplitude.Constants;
import com.martinet.emplitude.Global;
import com.martinet.emplitude.MainActivity;
import com.martinet.emplitude.Models.Lesson;
import com.martinet.emplitude.Dialog.DialogPicker;
import com.martinet.emplitude.Dialog.ColorPicker;
import com.martinet.emplitude.Sound.Son;
import com.martinet.emplitude.Tool.Response;
import com.martinet.emplitude.R;
import com.martinet.emplitude.Schelude.FragmentLesson;
import com.martinet.emplitude.Schelude.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class ActivitySchelude extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Response<String, String> {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMMM", Locale.FRENCH);
    private static final String LIMIT_PAGE = "L'emploi du temps n'a pas été chargé aussi loin.";
    private int HEIGHT;
    private Calendar calendar = Calendar.getInstance();
    private View viewAction;
    private FragmentActivity activity;
    private SwipeRefreshLayout swipe;
    private Toast toast;
    private RelativeLayout color;
    private Button button_picker;
    private ViewPager mPager;
    private SparseArray<Fragment> fragmentReference;

    private PagerAdapter mPagerAdapter;
    private GridLayout glHours;
    private Date firstDay;
    private Date lastDay;
    private int timeBefore;
    private int timeAfter;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        this.getActivity().setTitle("Emploi du temps");
        this.activity = getActivity();
        this.toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

        if(Global.global.getLessons() == null) {
            view = inflater.inflate(R.layout.schelude_empty, container, false);
            this.swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
            this.swipe.setOnRefreshListener(this);
            onRefresh();
            return view;
        }

        view = inflater.inflate(R.layout.emploi_du_temps, container, false);

        this.color = (RelativeLayout) view.findViewById(R.id.color);
        this.button_picker = (Button) color.findViewById(R.id.color_5);
        //Ajout du listener le mise a jour auto
        this.swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.swipe.setColorSchemeColors(getResources().getIntArray(R.array.spinner));
        this.swipe.setOnRefreshListener(this);
        this.HEIGHT = Constants.getHeight(getContext())+100;
        this.glHours = (GridLayout) view.findViewById(R.id.heures);
        this.firstDay = Lesson.getFirstDay().getDateBegin();
        this.lastDay = Lesson.getLastDay().getDateBegin();
        timeBefore = dayDifference(calendar.getTime(), firstDay);
        timeAfter = dayDifference(lastDay, calendar.getTime());

        if(timeBefore <  1) {
            timeBefore = 0;
            firstDay = new Date();
        }
        if(timeAfter < 1) {
            timeAfter = 0;
        }

        this.changeToolbar();
        this.loadCouleurs();
        this.loadHeures();
        this.setToolbarDateTitle(firstDay);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        mPager.getLayoutParams().height= Constants.getHeight(getContext());
        mPagerAdapter = new ScreenSlidePagerAdapter(activity.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.getLayoutParams().height = Constants.getHeight(getContext())+100;
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int page) {
                calendar = Calendar.getInstance();
                calendar.setTime(firstDay);
                calendar.add(Calendar.DATE, page);
                setToolbarDateTitle(calendar.getTime());
                colorBarVisibility(ViewGroup.GONE);
            }

            public void onPageScrollStateChanged(int state) {
                colorBarVisibility(ViewGroup.GONE);
            }
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                colorBarVisibility(ViewGroup.GONE);
            }
        });
        mPager.setCurrentItem(timeBefore);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem fav = menu.add(Menu.NONE, 0, Menu.NONE,"Choisir un jour.");
        fav.setIcon(R.drawable.ic_day_lite);
        fav.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(firstDay != null) {
                    DialogPicker dialogPicker = DialogPicker.newInstance(new Date(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, datePicker.getYear());
                            calendar.set(Calendar.MONTH, datePicker.getMonth());
                            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            long j = dayDifference(calendar.getTime(), firstDay);
                            mPager.setCurrentItem((int) j, true);
                        }
                    }, firstDay.getTime(), lastDay.getTime());
                    dialogPicker.show(activity.getFragmentManager(), "Choisir un jour");
                } else {
                    showToast("Impossible de choisir un jour car vous n'avez pas de cours.");
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public int dayDifference(Date firstDay, Date lastDay) {
        return (int) Math.ceil(TimeUnit.DAYS.convert(firstDay.getTime() - lastDay.getTime(), TimeUnit.MILLISECONDS));
    }

    public void changeToolbar(){
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        LayoutInflater vi = LayoutInflater.from(getContext());
        View v = vi.inflate(R.layout.emploi_action, null);
        this.viewAction = v;
        tool.addView(v);
        ImageButton pre = (ImageButton) v.findViewById(R.id.precedent);
        ImageButton next = (ImageButton) v.findViewById(R.id.suivant);
        pre.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mPager.getCurrentItem()-1;
                if(i < 0){
                    showToast(LIMIT_PAGE);
                }else{
                    mPager.setCurrentItem(i, true);
                }
            }
        });
        next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = mPager.getCurrentItem() + 1;
                if (getMaxItem() <= i) {
                    showToast(LIMIT_PAGE);
                } else {
                    mPager.setCurrentItem(i, true);
                }
            }
        });
    }

    public void loadHeures() {
        int hauteur= HEIGHT/12;
        TextView heureJournee;
        String[] heures = getResources().getStringArray(R.array.heures);
        for (String h : heures) {
            heureJournee = new TextView(getContext());
            heureJournee.setGravity(Gravity.CENTER_VERTICAL);
            heureJournee.setHeight(hauteur);
            heureJournee.setWidth(HEIGHT);
            heureJournee.setBackgroundResource(R.drawable.border_heure);
            heureJournee.setText(h);
            glHours.addView(heureJournee);
        }
    }


    public void refresh(){
        int index = mPager.getCurrentItem();
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(index);
    }

    //Lorsque l'utilisateur change de page on supprime la toolbar personnalisé
    public void onDestroyView(){
        super.onDestroyView();
        ViewGroup tool = (ViewGroup) getActivity().findViewById(R.id.tool);
        tool.removeView(viewAction);
    }

    //Lorsque l'utilisateur glisse son doigt vers le vas
    public void onRefresh() {
        swipe.setRefreshing(true);
        new Schedule().load(this);
    }

    public void showToast(String value) {
        toast.setText(value);
        toast.show();
    }

    public FragmentLesson getFragment(){
        return ((FragmentLesson)fragmentReference.get(mPager.getCurrentItem()));
    }

    public int getMaxItem() {
        return timeAfter + timeBefore + 2 ;
    }

    public void loadCouleurs() {

        final int[] colorBar = getResources().getIntArray(R.array.colorBar);
        int id;
        Vector<Button> button = new Vector<>();
        for (int i = 1; i < 5; i++) {
            id = getResources().getIdentifier("color_" + i, "id", getActivity().getPackageName());
            button.add((Button) color.findViewById(id));
            button.get(i - 1).getBackground().setColorFilter(colorBar[i - 1], PorterDuff.Mode.MULTIPLY);
            button.get(i - 1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    LinearLayout ll = (LinearLayout) color.findViewById(R.id.color_linear);
                    int k = ll.indexOfChild(b);
                    colorBarVisibility(View.GONE);
                    getFragment().setColorButton(colorBar[k]);
                }
            });
        }

        //Lorque l'utilisateur selectionne le bouton personnalisé : ouverture du color picker
        button_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBarVisibility(View.GONE);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ColorPicker editNameDialog = new ColorPicker();
                editNameDialog.show(fm, "ColorPicker Picker");
            }
        });
    }

    //Change la visibilité de la barre des couleurs
    public void colorBarVisibility(int mode){
        color.setVisibility(mode);
    }

    public void setToolbarDateTitle(Date date){
        TextView jour = (TextView) viewAction.findViewById(R.id.jour);
        jour.setText(simpleDateFormat.format(date).toUpperCase());
    }

    @Override
    public void onSuccess(String value) {
        this.showToast(value);
        swipe.setRefreshing(false);
        if(view.findViewById(R.id.empty) == null) {
            refresh();
        } else {
            ((MainActivity) getActivity()).changeFragment(null, ActivitySchelude.class);
        }
    }

    @Override
    public void onError(String message) {
        this.showToast(message);
        swipe.setRefreshing(false);
    }

    /**
     * Classe interne pour la gestion de tout les jours avec le slider
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentReference = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            calendar = Calendar.getInstance();
            calendar.setTime(firstDay);
            calendar.add(Calendar.DATE, position);
            FragmentLesson j = FragmentLesson.newInstance(calendar.getTime());
            fragmentReference.put(position, j);
            return j;
        }

        @Override
        public int getCount() {
            return getMaxItem();
        }
    }
}
