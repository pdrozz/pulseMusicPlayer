package com.pdrozz.pulsemusicplayer.fragment;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.utils.PlayerManager;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPlayer extends Fragment {

    public FragmentPlayer() {
        // Required empty public constructor
    }

    public FragmentPlayer(MusicModel music){
        this.music=music;
        Log.i("FRAGMENT PLAYER", "INICIADO ");
    }

    private MusicModel music;

    private ImageView imageMusic;
    private TextView musicName,artistName;

    private MediaPlayer media;

    private Activity activity;

    private Uri uri;

    private File picture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_player, container, false);
        activity=getActivity();
        if (music!=null){
            uri=Uri.parse(music.getPath());
            picture=new File(activity.getFilesDir(),music.getName().toLowerCase()+".jpeg");
            configWidgets(v);
            setValues();
            setPicture();
        }

        return v;
    }

    private void setPicture(){
        Glide.with(activity).load(picture).placeholder(R.drawable.bg_gradient).into(imageMusic);
    }

    private void configWidgets(View v){
        imageMusic=v.findViewById(R.id.imageMusic);
        musicName=v.findViewById(R.id.txtArtist);
        artistName=v.findViewById(R.id.txtMusicName);
    }

    private void setValues(){
        if (music.getArtist().equals("<unknown>")){
            music.setArtist("Desconhecido");
        }
        musicName.setText(music.getName());
        artistName.setText(music.getArtist());
    }
}
