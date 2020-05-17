package com.pdrozz.pulsemusicplayer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.PagerAdapterPlayer;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.notification.NotificationUtil;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity {

    public static String GET_ACTION="get_action";
    public static final int ONLY_OPEN=3;

    private ViewPager viewPager;
    private TextView txtCurrentTime,txtTotalTime;
    private ImageButton btnPlayPause,btnNext,btnPrevious;
    private Button btnVibe;
    private SeekBar seekBar;

    private PagerAdapterPlayer adapter;

    private String NAME_PLAYLIST;
    private int POSITION;
    private int ACTION;

    private MusicModel musicModel;
    private List<MusicModel> listMusic=new ArrayList<>();

    private DAOplaylist daOplaylist;

    private Timer seekTimer;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        configWidgets();
        getBundleValues();
        configBtnPlayOnClick();

        daOplaylist=new DAOplaylist(this);
        listMusic=daOplaylist.getPlaylistItems(NAME_PLAYLIST);

        configViewPager();

        if (ACTION!=ONLY_OPEN){
            if (!PlayerManager.isPlaying()){
                PlayerManager.setListMusic(listMusic);
                PlayerManager.setCurrentPlaylistName(NAME_PLAYLIST);
                PlayerManager.startMusic(this,POSITION);
            }else if(POSITION!=PlayerManager.currentMusicPosition){
                PlayerManager.setListMusic(listMusic);
                PlayerManager.setCurrentPlaylistName(NAME_PLAYLIST);
                PlayerManager.startMusic(this,POSITION);
        }}
        configSeekBar();
        configTimer();
        configSeekBarClick();
        configPlayQueue();
        switchPlayImage();

        //WidgetsListeners
        configNexttOnClick();
        configBtnPlayOnClick();
        configPreviousOnClick();
        configVibeClick();


        //broadcast
        configBroadCast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            registerReceiver(broadcastReceiver, new IntentFilter("MUSICS_PULSE"));
        }


        //viewPagerConfig
        viewPager.setCurrentItem(POSITION);
        configViewPagerListener();
    }
    private void configBroadCast(){
        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                viewPager.setCurrentItem(PlayerManager.currentMusicPosition);
                switchPlayImage();
            }
        };
    }

    private void configViewPagerListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PlayerActivity.this.POSITION=position;
                startMusic("PAGELISTENER");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void configVibeClick(){
        btnVibe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean haverPermission= ContextCompat.checkSelfPermission(PlayerActivity.this,Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;
                if (!haverPermission){
                    showAlertVibe();
                }
                else{
                    Intent intentVibe=new Intent(PlayerActivity.this,VibeActivity.class);
                    intentVibe.putExtra("name",musicModel.getName());
                    startActivity(intentVibe);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result:grantResults){
            if (result==RESULT_OK){
                startActivity(new Intent(this,VibeActivity.class));
            }else{
                Toast.makeText(this, "Você precisa aceitar as permissões para utilizar todos os recursos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showAlertVibe(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Permissões para o modo Vibe");
        builder.setMessage("O modo vibe precisa da permissão de áudio para poder criar e gerir o visualizador");
        builder.setPositiveButton("Concordo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] permissao={Manifest.permission.RECORD_AUDIO};
                PermissionUtil.getPermission(permissao,PlayerActivity.this,300);
            }
        });
        builder.setNegativeButton("Não concordo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void configPlayQueue(){
        PlayerManager.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (PlayerActivity.this.POSITION<PlayerManager.playlistSize){
                    PlayerActivity.this.POSITION++;
                    startMusic("PlayQueue");
                }
            }
        });
    }

    private void startMusic(String who){
        MediaPlayer media=MediaPlayer.create(this,Uri.parse(listMusic.get(POSITION).getPath()));
        PlayerManager.setMediaPlayer(media,who);
        PlayerManager.currentMusic=listMusic.get(POSITION);
        PlayerManager.currentPlaylistName=NAME_PLAYLIST;
        PlayerManager.currentMusicPosition=POSITION;

        PlayerManager.start();

        initStart();
    }
    private void initStart(){
        setTxtTotalTime();
        switchPlayImage();
        configTimer();


        viewPager.setCurrentItem(PlayerManager.currentMusicPosition);
    }

    private void configSeekBar(){
        seekBar.setMax(PlayerManager.getMediaPlayer().getDuration());
    }

    private void configSeekBarClick(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    PlayerManager.seekTo(progress);

                }
                setTxtCurrentTime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setTxtCurrentTime(){
        int time=PlayerManager.getMediaPlayer().getCurrentPosition();
        int minutes=time/1000/60;
        String seconds=(time/1000%60)+"";
        if (seconds.length()==1){
            seconds="0"+seconds;
        }
        txtCurrentTime.setText(minutes+":"+seconds);
    }
    private void setTxtTotalTime(){
        int time=PlayerManager.getMediaPlayer().getDuration();
        int minutes=time/1000/60;
        String seconds=(time/1000%60)+"";
        if (seconds.length()==1){
            seconds="0"+seconds;
        }
        txtTotalTime.setText(minutes+":"+seconds);
    }

    private void configNexttOnClick(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerManager.next(PlayerActivity.this);
                initStart();
            }
        });
    }

    private void configPreviousOnClick(){
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerManager.previous(PlayerActivity.this);
                initStart();
            }
        });
    }

    private void configBtnPlayOnClick(){
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayPause();
            }
        });
    }

    private void setPlayPause(){
        if (PlayerManager.getMediaPlayer()!=null){
            if (PlayerManager.getMediaPlayer().isPlaying()){
                PlayerManager.playPause(PlayerActivity.this);
                switchPlayImage();
            }
            else{
                PlayerManager.playPause(PlayerActivity.this);
                switchPlayImage();
            }
        }
    }

    private void switchPlayImage(){
        if (PlayerManager.getMediaPlayer().isPlaying()){
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        }else{
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void getBundleValues(){
        Bundle dados=getIntent().getExtras();
        musicModel=dados.getParcelable(PlaylistActivity.MUSIC_ITEM);
        NAME_PLAYLIST=dados.getString(PlaylistActivity.NAME_PLAYLIST);
        POSITION=dados.getInt(PlaylistActivity.POSITION_ITEM);
        ACTION=dados.getInt(PlayerActivity.GET_ACTION);
    }

    private void configViewPager(){
        adapter=new PagerAdapterPlayer(getSupportFragmentManager(),
                this,
                listMusic);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void configWidgets(){
        viewPager=findViewById(R.id.viewPagerPlayer);

        txtCurrentTime=findViewById(R.id.txtCurrentTime);
        txtTotalTime=findViewById(R.id.txtTotalTime);

        btnPlayPause=findViewById(R.id.btnPlayPause);
        btnVibe=findViewById(R.id.btnVibe);
        btnNext=findViewById(R.id.btnNext);
        btnPrevious=findViewById(R.id.btnPrevious);

        seekBar=findViewById(R.id.seekTime);
    }
    private void configTimer(){
        seekTimer=new Timer();
        seekTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (PlayerManager.validate()){
                    int progress=0;
                    try {
                        progress=PlayerManager.mediaPlayer.getCurrentPosition();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    seekBar.setProgress(progress);
                }else {
                    seekTimer.cancel();
                }
            }
        },0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
