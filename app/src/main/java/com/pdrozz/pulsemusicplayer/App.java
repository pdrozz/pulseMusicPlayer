package com.pdrozz.pulsemusicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.pdrozz.pulsemusicplayer.notification.NotificationUtil;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;



public class App extends Application {

    private NotificationManager notificationManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        configBroadCast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("MUSICS_PULSE"));
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(broadcastReceiver);
        PlayerManager.release();
        PlayerManager.cancelNotificationPlayer();
        PlayerManager.releaseClass();
    }


    private void configBroadCast(){
        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getExtras().getString("actionname");
                switch (action){
                    case NotificationUtil.ACTION_NEXT:
                        next();
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



    private void playOrPause(){
        PlayerManager.playPause(getApplicationContext());
    }

    private void next(){
        PlayerManager.next(getApplicationContext());
    }

    private void previous(){
        PlayerManager.previous(getApplicationContext());
    }

}