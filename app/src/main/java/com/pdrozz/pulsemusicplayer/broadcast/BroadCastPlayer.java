package com.pdrozz.pulsemusicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadCastPlayer extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("MUSICS_PULSE")
                .putExtra("actionname", intent.getAction()));
        Log.i("BROARCAST", "onReceive: recebido -> action:"+intent.getAction());
    }
}
