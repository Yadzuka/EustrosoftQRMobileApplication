package ru.eustrosoft.androidqr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// TODO: use in future
public abstract class AbstractBaseHelper extends SQLiteOpenHelper {
    private static final int DEFAULT_VERSION = 1;

    public AbstractBaseHelper(Context context, String dbName) {
        super(context, dbName, null, DEFAULT_VERSION);
    }

    public AbstractBaseHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
    }

    @Override
    public abstract void onCreate(SQLiteDatabase db);

    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
