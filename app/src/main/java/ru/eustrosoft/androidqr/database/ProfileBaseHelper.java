package ru.eustrosoft.androidqr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "profile.db";

    public ProfileBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ProfileDBSchema.ProfileTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ProfileDBSchema.ProfileTable.Cols.UUID + ", " +
                ProfileDBSchema.ProfileTable.Cols.DATE + ", " +
                ProfileDBSchema.ProfileTable.Cols.NAME + ", " +
                ProfileDBSchema.ProfileTable.Cols.PASSWORD + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
