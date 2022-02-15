package com.example.fa_keerthi_pavan_valluri_c0826625_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DB_NAME = "MAP_DATABASE";

    private static final String TABLE_LOCATION = "LOCATIONS";
    private static final String LOC_ADDRESS = "ADDRESS";
    private static final String LOC_LONG = "LONGITUDE";
    private static final String LOC_LAT = "LATITUDE";
    private static final String LOC_ID = "ID";


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION + "(" + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LOC_ADDRESS + " TEXT," + LOC_LAT + " INTEGER," + LOC_LONG + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
    }
}
