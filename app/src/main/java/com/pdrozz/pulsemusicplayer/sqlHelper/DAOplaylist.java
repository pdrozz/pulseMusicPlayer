package com.pdrozz.pulsemusicplayer.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class DAOplaylist {

    private Context context;
    private SQLiteDatabase write,read;

    public DAOplaylist(Context context){
        SqlHelper sqlHelper=new SqlHelper(context);
        this.context=context;
        write=sqlHelper.getWritableDatabase();
        read=sqlHelper.getReadableDatabase();
    }

    public boolean createPlaylist(String nome, String descricao){
        ContentValues cv=new ContentValues();
        cv.put("nome",nome);
        if (descricao!=null && !descricao.equals(""))
        {
            cv.put("descricao",descricao);
        }
        cv.put("dataCriacao",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try{
            write.insert(SqlHelper.TABLE_PLAYSLISTS_NAMES,null,cv);
            write.execSQL("CREATE TABLE IF NOT EXISTS "+nome+SqlHelper.sqlPlaylistItems);
            Toast.makeText(context, "Criado com sucesso", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("insert DATABASE ERROR", "createPlaylist: "+e);
            Toast.makeText(context, "Erro ao criar"+e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean inserirNaPlaylist(String namePlaylist, MusicModel music){
        ContentValues cv=new ContentValues();
        cv.put("name",music.getName());
        cv.put("path",music.getPath());
        cv.put("artist",music.getArtist());
        cv.put("album",music.getAlbum());
        cv.put("duration",music.getDuration());
        try{
            write.insert(namePlaylist,null,cv);
            write.close();
            return true;
        }catch (Exception e){
            Log.e("error sqlite", "inserirNaPlaylist: "+e );
            write.close();
            return false;
        }
    }

    public List<PlaylistModel> getPlaylists(){
        List<PlaylistModel> list=new ArrayList<>();
        String sql="select * from "+SqlHelper.TABLE_PLAYSLISTS_NAMES+";";
        Cursor c=read.rawQuery(sql,null);
        while (c.moveToNext()){
            PlaylistModel model=new PlaylistModel();
            model.setName(c.getString(c.getColumnIndex("nome")));
            model.setDesc(c.getString(c.getColumnIndex("descricao")));
            model.setId(c.getInt(c.getColumnIndex("id")));
            model.setCreateDate(c.getString(c.getColumnIndex("dataCriacao")));
            list.add(model);
        }
        c.close();
        return list;
    }

    public boolean deletePlaylist(String name){
        String sqlDropItem="DROP TABLE "+name+";";
        String sqlDropNames="delete from "+SqlHelper.TABLE_PLAYSLISTS_NAMES+ " where nome='"+name+"';";

        Log.i("SQLPLAYLIST", "sqlDropItem: "+sqlDropItem);
        Log.i("SQLPLAYLIST", "sqlDropNames: "+sqlDropNames);

        try{
        write.execSQL(sqlDropItem);
        write.execSQL(sqlDropNames);
            write.close();
            return true;

        }catch (Exception e){
            Log.i("SQLPLAYLIST", "deletePlaylist: ERRO"+e);
            write.close();
            return false;
        }


    }
}
