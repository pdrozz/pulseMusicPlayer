package com.pdrozz.pulsemusicplayer.model;

import com.pdrozz.pulsemusicplayer.widget.favbutton.FavButton;

import java.util.List;

public class MusicModel {

    private String name,path,artist,album,duration;


    public static List<MusicModel> listMusic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public MusicModel() {
    }
}
