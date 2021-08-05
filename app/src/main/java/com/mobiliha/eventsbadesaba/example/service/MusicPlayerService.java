package com.mobiliha.eventsbadesaba.example.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

/**
 * An example of a started service.
 */
public class MusicPlayerService extends Service {

    public static final String EXTRA_MUSIC_URI = "music_uri";

    private MediaPlayer player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(player != null)
            player.stop();

        Uri uri = intent.getParcelableExtra(EXTRA_MUSIC_URI);

        player = MediaPlayer.create(this, uri);
        player.setLooping(true);
        player.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        player.stop();
    }
}