package com.example.martinet.Emplitude.Reveil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.martinet.Emplitude.R;

/**
 * Created by root on 06/02/16.
 */
public class AudioPlayerDemoActivity  extends ListActivity{
    private final String MEDIA_PATH = new String("/storage/emulated/O/syncr/Compilations/Reload/");
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();
    private int currentPosition = 0;
    ArrayAdapter<String> songList;
    private Button brAnnulerSon;
    private Button brValiderSon;
    private ListView listView;
    public SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String nomSon;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_audio_player_demo);
        updateSongList();
        brAnnulerSon = (Button) findViewById(R.id.brAnnulerSon);
        brValiderSon = (Button) findViewById(R.id.brValiderSon);
        View.OnClickListener listenerAnnuler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        sharedpreferences = getSharedPreferences(ReveilActivity.MesPREFERENCES, Context.MODE_PRIVATE);
        this.editor = sharedpreferences.edit();

        View.OnClickListener listenerValider = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReveilActivity.btnSon.setText(sharedpreferences.getString(ReveilActivity.keySonChoisis,"Défaut"));
                finish();
            }
        };
        brAnnulerSon.setOnClickListener(listenerAnnuler);
        brValiderSon.setOnClickListener(listenerValider);
        /*listView = (ListView) findViewById(R.id.android_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nomSon = (String) listView.getItemAtPosition(position);
            }
        });
*/
    }



    //get the song list from sd-card
    public void updateSongList() {
        File home = new File(MEDIA_PATH);
        if (home.listFiles(new MyMP3Filter()).length > 0) {
            for (File file : home.listFiles(new MyMP3Filter())) {
                songs.add(file.getName());
            }
            songList = new ArrayAdapter<String>(this, R.layout.item, songs);
            setListAdapter(songList);
            //play the song from playSong method here we are passing song path to play
           // playSong(MEDIA_PATH + songs.get(currentPosition));

        }
    }

    //method play song
    private void playSong(String songPath) {
        try {
            mp.reset();
            mp.setDataSource(songPath);
            mp.prepare();
            mp.start();
            // Setup listener so next song starts automatically
            mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    nextSong();
                }
            });
        } catch (IOException e) {
        }
    }
    public void onListItemClick(ListView l, View v, int position, long id) {
        currentPosition = position;
        nomSon=(MEDIA_PATH + songs.get(position));
        editor.putString(ReveilActivity.keySonChoisis, songList.getItem(position));
        editor.commit();

    }

    //method to play next song from the list if size is grater than current
    private void nextSong() {
        if (++currentPosition >= songs.size()) {
            // Last song, just reset currentPosition
            currentPosition = 0;
        } else {
            // Play next song
            playSong(MEDIA_PATH + songs.get(currentPosition));
        }
    }

}

