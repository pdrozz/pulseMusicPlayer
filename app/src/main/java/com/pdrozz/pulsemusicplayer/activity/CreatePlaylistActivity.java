package com.pdrozz.pulsemusicplayer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.AdapterMusicFiles;
import com.pdrozz.pulsemusicplayer.fragment.HomeFragment;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatePlaylistActivity extends AppCompatActivity {

    private ImageView imagePlaylist;
    private ImageButton btnEditPicture;
    private EditText editName,editDesc;
    private Button criarPlaylist,cancel;
    private final int SELECT_PICTURE=30;
    private Bitmap imagem=null;
    private DAOplaylist dao;
    String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.create_playlist);
            configWidgetsCreatePlaylist();
            configCriarPlaylistClickListener();
            configEditPictureClickListener();


            configCancelClick();

            dao=new DAOplaylist(CreatePlaylistActivity.this);

    }

    private void configCriarPlaylistClickListener(){
        criarPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDados()){
                    dao.createPlaylist(editName.getText().toString(),"");
                    try {
                        saveImagePlaylistInternalStorage(editName.getText().toString(),imagem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void configEditPictureClickListener(){
        PermissionUtil.getPermission(permissoes,this,30);
        btnEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager())!=null)startActivityForResult(i,SELECT_PICTURE);
            }
        });
    }

    private boolean validateDados(){
        String name=editName.getText().toString();
        boolean nomeValido=!name.replace(" ","").equals("")&&name!=null;
        if (nomeValido) return true;
        else return false;
    }

    private void configWidgetsCreatePlaylist(){
        imagePlaylist=findViewById(R.id.imageCreatePlaylist);
        btnEditPicture=findViewById(R.id.button_pic_create);
        criarPlaylist=findViewById(R.id.buttonCriarPlaylist);
        cancel=findViewById(R.id.buttonCancel);
        editName=findViewById(R.id.textCreateName);

    }
    private void configCancelClick(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveImagePlaylistInternalStorage(String filename,Bitmap image) throws IOException {
        FileOutputStream fos=openFileOutput(filename.toLowerCase()+".jpeg",Context.MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,baos);
            fos.write(baos.toByteArray());
        }catch (Exception e)
        {
            Log.e("erro save picture", "saveImagePlaylistInternalStorage: "+e );
        }
        fos.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 3:
                    Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
                    break;
                case SELECT_PICTURE:
                    Uri local=data.getData();
                    try {
                        imagem= MediaStore.Images.Media.getBitmap(getContentResolver(),local);
                        if (imagem!=null)imagePlaylist.setImageBitmap(imagem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }
}
