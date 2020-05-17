package com.pdrozz.pulsemusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;
import com.gauravk.audiovisualizer.visualizer.BlobVisualizer;
import com.gauravk.audiovisualizer.visualizer.HiFiVisualizer;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;

import java.util.Timer;
import java.util.TimerTask;

public class VibeActivity extends AppCompatActivity {

    private TextView textMusic;
    private BlobVisualizer blast;
    private ProgressBar progressBar;
    private Timer seekTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibe);

        getSupportActionBar().hide();

        String name =getIntent().getExtras().getString("name");

        blast=findViewById(R.id.blast);
        textMusic=findViewById(R.id.textMusic);
        if(name!=null){
            textMusic.setText(name);
        }

        progressBar=findViewById(R.id.progressBar);
        configTimer();
        configSeekBar();


        PlayerManager.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
                seekTimer.cancel();
            }
        });


        int audioSessionId = PlayerManager.getMediaPlayer().getAudioSessionId();
        if (audioSessionId != -1)
            blast.setAudioSessionId(audioSessionId);
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
                    progressBar.setProgress(progress);
                }else {
                    seekTimer.cancel();
                }
            }
        },0,1000);
    }

    private void configSeekBar(){
        progressBar.setMax(PlayerManager.getMediaPlayer().getDuration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (blast != null)
            blast.release();
    }
}

