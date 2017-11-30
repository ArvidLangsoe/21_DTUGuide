package com.arvid.dtuguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jeppe on 23-11-2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(Context context, String DatabaseName) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rooms (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS rooms");
        onCreate(db);
    }

    public void addRoom(String name) {

        ContentValues values = new ContentValues(1);

        values.put("name", name);

        getWritableDatabase().insert("rooms", "name", values);
    }

    public Cursor getRooms() {
        String[] columns = new String[] { "_id", "name" };
        Cursor cursor = getReadableDatabase().query("rooms", columns, null, null, null, null, null);
        return cursor;
    }

    public void deleteAll() {
        getWritableDatabase().delete("rooms", null, null);
    }
}
