package com.pdrozz.pulsemusicplayer.model;

import java.util.ArrayList;
import java.util.List;

public class PlaylistModel {

    public static List<PlaylistModel> listPlaylist=new ArrayList<>();

    private String name, desc, picture, createDate;
    private List<MusicModel> listMusic;
    private int id;
    private int ACTION=0;

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


}
