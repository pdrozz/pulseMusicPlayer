package com.pdrozz.pulsemusicplayer.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.viewpager.widget.ViewPager;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.activity.PlayerActivity;
import com.pdrozz.pulsemusicplayer.activity.PlaylistActivity;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.notification.NotificationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerManager {

    //mediaplyer
    public static MediaPlayer mediaPlayer=null;

    //model
    public static List<MusicModel> listMusic=new ArrayList<>();
    public static MusicModel currentMusic=null;
    //var
    public static String currentPlaylistName=null;
    public static int currentMusicPosition;
    public static int playlistSize;
    public static Context currentNotificationContext;
    //timer
    static Timer timer=new Timer();
    static Random random=new Random();
    static List<Integer> played=new ArrayList<>();
    static int[] ints;


    public static void setCurrentNotificationContext(Context c){
        PlayerManager.currentNotificationContext=c;
    }

    public static void setListMusic(List<MusicModel> listMusic) {
        PlayerManager.listMusic = listMusic;
        PlayerManager.playlistSize=listMusic.size()-1;
    }

    public static void setCurrentPlaylistName(String currentPlaylistName) {
        PlayerManager.currentPlaylistName = currentPlaylistName;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void queue(){
        if (validate()){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }
    }

    public static void playShuffle(){
        ints=new int[playlistSize];
        int newInt;
        while (played.size()<playlistSize){
            do {
                newInt=random.nextInt(playlistSize);
            }while (!played.contains(newInt));
                played.add(newInt);
            }
    }

    static void createNotificationPlaying(Context context){
        setCurrentNotificationContext(context);
        NotificationUtil.createNotification(context, listMusic.get(PlayerManager.currentMusicPosition),
                R.drawable.ic_pause, PlayerManager.currentMusicPosition,playlistSize);
    }

    static void createNotificationPaused(Context context){
        setCurrentNotificationContext(context);
        NotificationUtil.createNotification(context, listMusic.get(PlayerManager.currentMusicPosition),
                R.drawable.ic_play, PlayerManager.currentMusicPosition,playlistSize);
    }

    public static void startMusic(Context context,int POSITION){
        release();
        MediaPlayer media=MediaPlayer.create(context, Uri.parse(listMusic.get(POSITION).getPath()));
        PlayerManager.setMediaPlayer(media,"");
        PlayerManager.currentMusic=listMusic.get(POSITION);
        PlayerManager.currentMusicPosition=POSITION;
        PlayerManager.start();

        createNotificationPlaying(context);
    }

    public static void setQueueMusicModel(){
        PlayerManager.currentMusic=listMusic.get(PlayerManager.currentMusicPosition);
    }

    public static void playSuffle(){

    }

    public static void next(Context context){
        if (validate()) {
            PlayerManager.currentMusicPosition++;
            if (PlayerManager.currentMusicPosition > listMusic.size() - 1) {
                PlayerManager.currentMusicPosition = 0;
            }
            startMusic(context, PlayerManager.currentMusicPosition);
            setQueueMusicModel();
            createNotificationPlaying(context);
        }
    }

    public static void previous(Context context){
        if (validate()){
            PlayerManager.currentMusicPosition--;
            if (PlayerManager.currentMusicPosition<0){
                PlayerManager.currentMusicPosition=listMusic.size()-1;
            }
            startMusic(context,PlayerManager.currentMusicPosition);
            setQueueMusicModel();
            createNotificationPlaying(context);
        }
    }

    public static void updateProgress(final ProgressBar progressBar){
        if (validate()){
            progressBar.setMax(mediaPlayer.getDuration());
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            },0,1000);
        }
    }

    public static void cancelProgress(){
        timer.cancel();
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer,String who) {
        release();
        PlayerManager.mediaPlayer = mediaPlayer;
        Log.i("setmusic", "setMediaPlayer: WHO"+who);
    }

    public static boolean isPlaying(){
        if (validate()){
            if (mediaPlayer.isPlaying()){
                return true;
            }
            else {return false;}
        }
        else {
            return false;
        }
    }

    public static int getTotalTime(){
        if (validate()){
            return mediaPlayer.getDuration();
        }
        else{
            return 0;
        }
    }

    public static void release(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            currentMusic=null;
        }
    }

    public static boolean playPause(Context context){
        if (validate()){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                createNotificationPaused(context);
                return false;
            }
            else{
                createNotificationPlaying(context);
                mediaPlayer.start();
                return true;
            }
        }else{
            Log.e("PLAYER MANAGER", "playPause: MEDIAPLAYER IS NULL or INDEFINIED" );
            return false;
        }
    }

    public static void seekTo(int to){
        if (validate()){
            mediaPlayer.seekTo(to);
        }
    }

    public static boolean validate(){
        if (mediaPlayer==null){
            return false;
        }else{
            return true;
        }
    }

    public static void start(){
        if (validate()){
            mediaPlayer.start();
        }
    }

    public static void cancelNotificationPlayer(){
        NotificationManager notificationManager;
        notificationManager=currentNotificationContext.getSystemService(NotificationManager.class);
        notificationManager.cancel(1);
    }

    public static void releaseClass(){
        PlayerManager.mediaPlayer=null;
        PlayerManager.listMusic=null;
        PlayerManager.currentMusic=null;
        PlayerManager.currentPlaylistName=null;
        PlayerManager.currentNotificationContext=null;
        PlayerManager.timer=null;
        PlayerManager.random=null;
        PlayerManager.played=null;
        PlayerManager.ints=null;
    }

}
