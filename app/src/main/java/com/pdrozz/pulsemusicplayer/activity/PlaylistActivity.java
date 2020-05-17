package com.pdrozz.pulsemusicplayer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.AdapterMusicFiles;
import com.pdrozz.pulsemusicplayer.adapter.PagerAdapterPlayer;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;
import com.pdrozz.pulsemusicplayer.utils.RecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    //widgets
    private ImageView imagePlaylist;
    private TextView textName,textAddMusics;
    private FloatingActionButton floatAdd;
    private FloatingActionButton floatPlay;
    private ProgressBar progressBar;
    //BarPlayer Widgets
    private TextView name,artist;
    private ImageButton playPause,fav,next;
    private ConstraintLayout player;
    //list
    private List<MusicModel> listMusic=new ArrayList<>();
    //recycler
    private RecyclerView recyclerFiles;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerItemClickListener recyclerItemClickListener;
    //adapter
    private AdapterMusicFiles adapter;
    //model
    private PlaylistModel playlist;

    public static final String NAME_PLAYLIST="playlist_name";
    public static final String MUSIC_ITEM="playlist_item";
    public static final String POSITION_ITEM="playlist_position";
    private DAOplaylist dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Bundle dados=getIntent().getExtras();
        playlist=dados.getParcelable("playlist");
        configWidgets();

        dao=new DAOplaylist(getApplicationContext());

        textName.setText(playlist.getName().toUpperCase());
        textName.setSelected(true);

        get();

        Glide.with(this).load(new File(getFilesDir(),playlist.getName().toLowerCase()+".jpeg"))
                .into(imagePlaylist);

        configRecycler();
        configRecyclerClick();

        configClickFab();

        recyclerFiles.addOnItemTouchListener(recyclerItemClickListener);

        player=findViewById(R.id.layout_playing);

    }

    private void configClickFab(){
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PlaylistActivity.this,FilesActivity.class);
                i.putExtra(NAME_PLAYLIST,playlist.getName());
                startActivity(i);
            }
        });
        floatPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayer(0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        get();
        adapter=new AdapterMusicFiles(listMusic,this);
        recyclerFiles.setAdapter(adapter);
        MusicModel model=PlayerManager.currentMusic;
        if (model!=null){
            //configBottomBarPlayer(model);
        }else {
            player.setVisibility(View.GONE);
        }
    }


    private void configBottomBarPlayer(MusicModel model){
        playPause=findViewById(R.id.btnPlayPause);
        fav=findViewById(R.id.btnLike);
        next=findViewById(R.id.btnNext);

        name=findViewById(R.id.txtMusicName);
        artist=findViewById(R.id.txtArtist);


        player.setVisibility(View.VISIBLE);
        name.setText(model.getName());
        name.setSelected(true);
        artist.setSelected(true);
        artist.setText(model.getArtist());

        configBarPlayerButtons();
        configPlayerClick();
    }
    private void configBarPlayerButtons(){
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagePlayPause();
                //PlayerManager.playPause(Pla);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void configPlayerClick(){
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayer();
            }
        });
    }

    private void openPlayer(){
        Intent player=new Intent(this,PlayerActivity.class);
        player.putExtra(PlaylistActivity.NAME_PLAYLIST,PlayerManager.currentPlaylistName);
        player.putExtra(PlaylistActivity.MUSIC_ITEM,PlayerManager.currentPlaylistName);
        player.putExtra(PlaylistActivity.POSITION_ITEM,PlayerManager.currentMusicPosition);
        startActivity(player);
    }

    private void setImagePlayPause(){
        if (PlayerManager.getMediaPlayer().isPlaying()){
            playPause.setImageResource(R.drawable.ic_play);
        }else{
            playPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private void get(){
        listMusic=dao.getPlaylistItems(playlist.getName());
    }

    private void configRecycler(){
        recyclerFiles=findViewById(R.id.recyclerMusics);
        layoutManager=new LinearLayoutManager(this);
        recyclerFiles.setLayoutManager(layoutManager);
        recyclerFiles.setHasFixedSize(true);
        adapter=new AdapterMusicFiles(listMusic,this);
        recyclerFiles.setAdapter(adapter);
    }

    private void startPlayer(int position){
        Intent i=new Intent(PlaylistActivity.this, PlayerActivity.class);
        i.putExtra(MUSIC_ITEM,listMusic.get(position));
        i.putExtra(NAME_PLAYLIST,playlist.getName());
        i.putExtra(POSITION_ITEM,position);
        startActivity(i);
    }

    private void configRecyclerClick(){
        recyclerItemClickListener=new RecyclerItemClickListener(this, recyclerFiles,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startPlayer(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
    }

    private void configWidgets(){
        floatAdd=findViewById(R.id.floatAdd);
        floatPlay=findViewById(R.id.floatPlay);

        imagePlaylist=findViewById(R.id.imagePlaylist);
        textName=findViewById(R.id.textViewNamePlaylist);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listMusic.clear();
    }


}
