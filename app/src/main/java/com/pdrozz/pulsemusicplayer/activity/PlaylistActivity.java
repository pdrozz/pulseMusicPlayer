package com.pdrozz.pulsemusicplayer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
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
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlaylistActivity extends AppCompatActivity {

    private ImageView imagePlaylist;
    private ImageButton btnEditPicture;
    private TextView textName;
    private EditText editName,editDesc;
    private Button criarPlaylist,cancel;
    private final int SELECT_PICTURE=30;
    private Bitmap imagem=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO implement individual activity to create playlists

        Bundle dados=getIntent().getExtras();
        if (dados.getBoolean("create")){
            setContentView(R.layout.create_playlist);
            configWidgetsCreatePlaylist();
            configCriarPlaylistClickListener();
            configEditPictureClickListener();
        }
        else{
            setContentView(R.layout.activity_playlist);
            PlaylistModel model=dados.getParcelable("playlist");
            //configWidgets
            imagePlaylist=findViewById(R.id.imagePlaylist);
            textName=findViewById(R.id.textName);
            textName.setText(model.getName().toUpperCase());
            Glide.with(this).load(new File(getFilesDir(),model.getName().toLowerCase()+".jpeg"))
            .into(imagePlaylist);

        }
    }

    private void configCriarPlaylistClickListener(){
        criarPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDados()){
                    DAOplaylist dao=new DAOplaylist(PlaylistActivity.this);
                    dao.createPlaylist(editName.getText().toString(),editDesc.getText().toString());
                    try {
                        saveImagePlaylistInternalStorage(editName.getText().toString(),imagem);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i("GEGE", "onClick: "+e);
                    }
                }
            }
        });
    }

    private void configEditPictureClickListener(){
        btnEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager())!=null)startActivityForResult(i,SELECT_PICTURE);
            }
        });
    }

    private boolean validateDados(){
        boolean nomeValido=!editName.getText().toString().replace(" ","").equals("") && editName.getText().toString()!=null;
        if (nomeValido) return true;
        else return false;
    }

    private void configWidgetsCreatePlaylist(){
        imagePlaylist=findViewById(R.id.imageCreatePlaylist);
        btnEditPicture=findViewById(R.id.button_pic_create);
        criarPlaylist=findViewById(R.id.buttonCriarPlaylist);
        cancel=findViewById(R.id.buttonCancel);
        editName=findViewById(R.id.textCreateName);
        editDesc=findViewById(R.id.textCreateDesc);

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
