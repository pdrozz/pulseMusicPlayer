package com.pdrozz.pulsemusicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.AdapterMusicFiles;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class FilesActivity extends AppCompatActivity {

    private String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};

    private RecyclerView recyclerFiles;
    private RecyclerView.LayoutManager layoutManager;

    private List<MusicModel> listMusic=new ArrayList<>();
    private AdapterMusicFiles adapter;
    private FloatingActionButton fab;
    private String namePlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        PermissionUtil.getPermission(permissoes,this,30);
        Bundle dados=getIntent().getExtras();
        namePlaylist=dados.getString(PlaylistActivity.NAME_PLAYLIST);

        configWidgets();

        scanMusicFiles();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                     adapter.saveSelected();
            }
        });
    }
    private void configRecycler(){
        layoutManager=new LinearLayoutManager(this);
        recyclerFiles.setLayoutManager(layoutManager);
        recyclerFiles.setHasFixedSize(true);
        adapter=new AdapterMusicFiles(listMusic,this);
        adapter.setNamePlaylist(namePlaylist);
        recyclerFiles.setItemViewCacheSize(17);
        recyclerFiles.setAdapter(adapter);
    }

    private void configWidgets(){
        recyclerFiles=findViewById(R.id.recyclerFiles);
        fab=findViewById(R.id.floatFiles);
    }

    private void scanMusicFiles(){
        String[] projection={MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor=null;

        try {
            Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor=getApplicationContext().getContentResolver().query(uri,projection,null,null,null);
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
                Log.e("TAG", "scanMusicFiles: "+model.getPath() );
            }
        }catch (Exception e){
            Log.i("TAG", "catch "+e);
        }
        finally {
            Log.i("TAG", "finally");
            cursor.close();
            configRecycler();
        }

    }
}
