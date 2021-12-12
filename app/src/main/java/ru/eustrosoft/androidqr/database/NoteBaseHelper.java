package ru.eustrosoft.androidqr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "noteBase.db";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteDBSchema.NoteTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NoteDBSchema.NoteTable.Cols.UUID + ", " +
                NoteDBSchema.NoteTable.Cols.DATE + ", " +
                NoteDBSchema.NoteTable.Cols.TIME + ", " +
                NoteDBSchema.NoteTable.Cols.TITLE + ", " +
                NoteDBSchema.NoteTable.Cols.TEXT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
