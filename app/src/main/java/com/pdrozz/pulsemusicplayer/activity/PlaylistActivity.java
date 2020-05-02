package com.pdrozz.pulsemusicplayer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.pdrozz.pulsemusicplayer.R;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle dados=getIntent().getExtras();
        if (dados.getBoolean("create")){
            setContentView(R.layout.create_playlist);
        }
        else{
            setContentView(R.layout.activity_playlist);
        }





        Intent    i=new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if(i.resolveActivity(getPackageManager())!=null){
            startActivityForResult(i,3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 3:
                    Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }
}
