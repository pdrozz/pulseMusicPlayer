package com.pdrozz.pulsemusicplayer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.broadcast.BroadCastPlayer;
import com.pdrozz.pulsemusicplayer.model.MusicModel;

public class NotificationUtil extends Notification {



        public static final String CHANNEL_ID = "channel1";

        public static final String ACTION_PREVIUOS = "actionprevious";
        public static final String ACTION_PLAY = "actionplay";
        public static final String ACTION_NEXT = "actionnext";

        public static Notification notification;

        public static void createNotification(Context context, MusicModel track, int playbutton, int pos, int size){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                MediaSessionCompat mediaSessionCompat = new MediaSessionCompat( context, "pulsePlayer");

                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add);

                PendingIntent pendingIntentPrevious;

                int drw_previous;
                if (pos == 0){
                    pendingIntentPrevious = null;
                    drw_previous = 0;
                } else {
                    Intent intentPrevious = new Intent(context, BroadCastPlayer.class)
                            .setAction(ACTION_PREVIUOS);
                    pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                            intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
                    drw_previous = R.drawable.ic_previous;
                }

                Intent intentPlay = new Intent(context, BroadCastPlayer.class)
                        .setAction(ACTION_PLAY);
                PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                        intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

                PendingIntent pendingIntentNext;
                int drw_next;
                    Intent intentNext = new Intent(context, BroadCastPlayer.class)
                            .setAction(ACTION_NEXT);
                    pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                            intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
                    drw_next = R.drawable.ic_next;


                //create notification
                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_favorite)
                        .setContentTitle(track.getName())
                        .setContentText(track.getArtist())
                        .setLargeIcon(icon)
                        .setOnlyAlertOnce(true)//show notification for only first time
                        .setShowWhen(false)
                        .addAction(drw_previous, "Previous", pendingIntentPrevious)
                        .addAction(playbutton, "Play", pendingIntentPlay)
                        .addAction(drw_next, "Next", pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
                                .setMediaSession(mediaSessionCompat.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build();

                notificationManagerCompat.notify(1, notification);

            }
        }

}
