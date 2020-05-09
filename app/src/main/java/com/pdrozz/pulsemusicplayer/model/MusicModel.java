package com.pdrozz.pulsemusicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pdrozz.pulsemusicplayer.widget.favbutton.FavButton;

import java.util.List;

public class MusicModel implements Parcelable {

    private String name,path,artist,album,duration;
    private boolean selected=false;
    private int id;

    protected MusicModel(Parcel in) {
        name = in.readString();
        path = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        selected = in.readByte() != 0;
        id = in.readInt();
    }

    public static final Creator<MusicModel> CREATOR = new Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeInt(id);
    }
}
