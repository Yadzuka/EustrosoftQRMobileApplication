package ru.eustrosoft.androidqr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.eustrosoft.androidqr.database.ScanItemDBSchema.ScanItemTable;

public class ScanItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "scanItemBase.db";

    public ScanItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ScanItemTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ScanItemTable.Cols.UUID + ", " +
                ScanItemTable.Cols.DATE + ", " +
                ScanItemTable.Cols.TIME + ", " +
                ScanItemTable.Cols.TEXT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
