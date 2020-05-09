package com.pdrozz.pulsemusicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PlaylistModel implements Parcelable {

    public static List<PlaylistModel> listPlaylist=new ArrayList<>();

    private String name, desc, picture, createDate;
    private List<MusicModel> listMusic;
    private int id;
    public final static int ACTION_CREATE=1;
    public final static int ACTION_DEFAULT=0;
    private int ACTION=ACTION_DEFAULT;


    public PlaylistModel(Parcel in) {
        name = in.readString();
        desc = in.readString();
        picture = in.readString();
        createDate = in.readString();
        id = in.readInt();
        ACTION = in.readInt();
    }

    public static final Creator<PlaylistModel> CREATOR = new Creator<PlaylistModel>() {
        @Override
        public PlaylistModel createFromParcel(Parcel in) {
            return new PlaylistModel(in);
        }

        @Override
        public PlaylistModel[] newArray(int size) {
            return new PlaylistModel[size];
        }
    };

    public PlaylistModel() {

    }

    public int getACTION() {
        return ACTION;
    }

    public void setACTION(int ACTION) {
        this.ACTION = ACTION;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<MusicModel> getListMusic() {
        return listMusic;
    }

    public void setListMusic(List<MusicModel> listMusic) {
        this.listMusic = listMusic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(picture);
        dest.writeString(createDate);
        dest.writeInt(id);
        dest.writeInt(ACTION);
    }
}
