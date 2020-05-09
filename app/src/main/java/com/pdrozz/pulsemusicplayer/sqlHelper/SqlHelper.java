package com.pdrozz.pulsemusicplayer.sqlHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlHelper  extends SQLiteOpenHelper {

    public static String DB_NAME="starDB";
    public static int DB_VERSION=1;
    public static String TABLE_PLAYSLISTS_NAMES="TABLE_PLAYSLISTS_NAMES";
    public static String TABLE_PLAYSLISTS_ITEMS="TABLE_PLAYSLISTS_ITEMS";

    private String sqlPlaylistName="CREATE TABLE IF NOT EXISTS "+TABLE_PLAYSLISTS_NAMES+
            " (id INTEGER PRIMARY KEY AUTOINCREMENT,nome VARCHAR not null, descricao VARCHAR,dataCriacao date)";

    public static String sqlPlaylistItems=" (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nome VARCHAR not null," +
            " artist VARCHAR, duration varchar, uri VARCHAR not null,album VARCHAR)";

    public SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlPlaylistName);
        //db.execSQL(sqlPlaylistItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
