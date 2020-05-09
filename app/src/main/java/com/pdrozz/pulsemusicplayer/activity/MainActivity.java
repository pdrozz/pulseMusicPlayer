package com.pdrozz.pulsemusicplayer.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.PagerAdapterTabs;
import com.pdrozz.pulsemusicplayer.broadcast.BroadCastPlayer;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.notification.NotificationUtil;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private FloatingActionButton fabMain;
    private ConstraintLayout player;
    private ProgressBar progressPlayer;

    //BarPlayer Widgets
    private TextView name,artist;
    private ImageButton playPause,fav,next;

    private NotificationManager notificationManager;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configWidgets();
        setupTabsViewPager();

        configFabMainClickListener();


        player=findViewById(R.id.layout_playing);

        configBroadCast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("MUSICS_PULSE"));
        }

    }

    private void configBroadCast(){
        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getExtras().getString("actionname");
                switch (action){
                    case NotificationUtil.ACTION_NEXT:
                        next();
                        setValuesBottomPlayerWidgets();
                        break;
                    case NotificationUtil.ACTION_PLAY:
                        playOrPause();
                        break;
                    case NotificationUtil.ACTION_PREVIUOS:
                        previous();
                        break;
                }
            }
        };
    }

    private void playOrPause(){
        PlayerManager.playPause(getApplicationContext());
    }

    private void next(){
        PlayerManager.next(getApplicationContext());
    }

    private void previous(){
        PlayerManager.previous(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PlayerManager.currentMusic!=null){
            configBottomPlayerWidgets();
            setValuesBottomPlayerWidgets();
            configBarPlayerButtons();
            configPlayerClick();
            PlayerManager.updateProgress(progressPlayer);
        }else {
            player.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        PlayerManager.release();

    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=
                new NotificationChannel(NotificationUtil.CHANNEL_ID,
                        "CHANEL_PULSE",
                        NotificationManager.IMPORTANCE_LOW);

            notificationManager=getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void configBottomPlayerWidgets(){
        playPause=findViewById(R.id.btnPlayPause);
        fav=findViewById(R.id.btnLike);
        next=findViewById(R.id.btnNext);

        name=findViewById(R.id.txtMusicName);
        artist=findViewById(R.id.txtArtist);

        progressPlayer=findViewById(R.id.progressBar);


    }

    private void setValuesBottomPlayerWidgets(){
        if (PlayerManager.validate()){
            if (name!=null){
                MusicModel model=PlayerManager.currentMusic;
                player.setVisibility(View.VISIBLE);
                name.setText(model.getName());
                name.setSelected(true);
                artist.setSelected(true);
                artist.setText(model.getArtist());
            }
        }
    }

    private void configBarPlayerButtons(){
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImagePlayPause();
                PlayerManager.playPause(MainActivity.this);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerManager.next(MainActivity.this);
                setValuesBottomPlayerWidgets();
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

    private void configFabMainClickListener(){
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreatePlaylistActivity.class).putExtra("create",true));
            }
        });
    }

    private void setupTabsViewPager(){
        PagerAdapterTabs pagerAdapterTabs = new PagerAdapterTabs(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapterTabs);

        tabs.setupWithViewPager(viewPager);
    }

    private void configWidgets(){
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        fabMain = findViewById(R.id.fab);
    }
}