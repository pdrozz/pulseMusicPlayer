package com.pdrozz.pulsemusicplayer.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pdrozz.pulsemusicplayer.fragment.FragmentPlayer;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapterPlayer extends FragmentPagerAdapter {

    private List<MusicModel> listMusic=new ArrayList<>();
    private List<FragmentPlayer> listFragment=new ArrayList<>();
    private Activity activity;
    private DAOplaylist dao;

    public void setList(List<MusicModel> listMusic){
        this.listMusic=listMusic;

    }

    private void scanMusicFiles(){

        Log.e("TAG", "scan view pager iniciado ");
        String[] projection={MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor=null;

        try {
            Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor=activity.getContentResolver().query(uri,projection,null,null,null);
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                Log.i("TAG", "CURSOR INICIADO ");
                MusicModel model=new MusicModel();
                String path=cursor.getString(2);
                Log.i("TAG", "path:"+path);
                model.setName(cursor.getString(0));
                model.setArtist( cursor.getString(1));
                model.setPath(path);
                model.setAlbum( cursor.getString(3));
                model.setDuration(cursor.getString(4));
                listMusic.add(model);
                Log.e("OPAOPA", "scanMusicFiles: "+model.getPath() );
            }
        }catch (Exception e){
            Log.e("TAG", "catch "+e);
        }
        finally {
            Log.e("TAG", "finally");
            cursor.close();
        }

    }


    public PagerAdapterPlayer(@NonNull FragmentManager fm, Activity activity,List<MusicModel> listMusic) {
        super(fm);
        this.activity=activity;
        this.listMusic=listMusic;
        configFragments();
        Log.e("PLAYERPLAYER", "PagerAdapterPlayer: inicializado" );
    }

    public PagerAdapterPlayer(@NonNull FragmentManager fm, int behavior,List<MusicModel> list) {
        super(fm, behavior);
        this.listMusic=list;
    }

    private void configFragments(){
        for (int i=0;i<listMusic.size();i++){
            listFragment.add(new FragmentPlayer(listMusic.get(i)));
            Log.v("PLAYERPLAYER", "ADD "+listMusic.get(i).getName() );
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listMusic.size();
    }
}
