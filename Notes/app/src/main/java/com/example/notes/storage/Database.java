package com.example.notes.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.example.notes.adapter.NoteAdapter;
import com.example.notes.interfaces.DBInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Database extends SQLiteOpenHelper implements DBInterface {
    private static final String dbName = "Notes.db";

    public Database(@Nullable Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableCreateQuery = "Create table Notes (id integer primary key autoincrement,title String, createTime String,color String)";
        db.execSQL(tableCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertNote(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
        String creationTime = sdf.format(new Date());
        ContentValues contentValues = new ContentValues();
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        contentValues.put("title", title);
        contentValues.put("createTime", creationTime);
        contentValues.put("color", String.valueOf(color));
        long status = db.insert("Notes", null, contentValues);
        if (status == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteNote(String title) {
        boolean statusDeleted = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Notes WHERE title=?", new String[]{title});
        if (cursor.getCount() > 0) {
            long status = db.delete("Notes", "title=?", new String[]{title});
            if (status == -1) {
                statusDeleted = false;
            } else {
                statusDeleted = true;
            }
        }
        return statusDeleted;
    }

    public Cursor getAllValues() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Notes", null);
        return cursor;
    }


    @Override
    public NoteAdapter insertNote(Context ctx, ArrayList<String> title, ArrayList<String> color, ArrayList<String> creationDate) {
        NoteAdapter noteAdapter = new NoteAdapter(ctx, color, title, creationDate);
        return noteAdapter;
    }
}
