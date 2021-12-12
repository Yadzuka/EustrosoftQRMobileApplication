package ru.eustrosoft.androidqr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.database.ScanItemBaseHelper;
import ru.eustrosoft.androidqr.database.ScanItemCursorWrapper;
import ru.eustrosoft.androidqr.database.ScanItemDBSchema.ScanItemTable;

public class ScanItemLab {
    private static ScanItemLab sScanItemLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ScanItemLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ScanItemBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static ScanItemLab get(Context context) {
        if (sScanItemLab == null)
            sScanItemLab = new ScanItemLab(context);

        return sScanItemLab;
    }

    private static ContentValues getContentValues(ScanItem ScanItem) {
        ContentValues values = new ContentValues();
        values.put(ScanItemTable.Cols.UUID, ScanItem.getId().toString());
        values.put(ScanItemTable.Cols.DATE, ScanItem.getDate().getTime());
        values.put(ScanItemTable.Cols.TIME, ScanItem.getTime().getTime());
        values.put(ScanItemTable.Cols.TEXT, ScanItem.getText());

        return values;
    }

    // todo: remove
    public void updateScanItem(ScanItem scanItem) {
        String uuidString = scanItem.getId().toString();
        ContentValues values = getContentValues(scanItem);

        mDatabase.update(ScanItemTable.NAME, values,
                ScanItemTable.Cols.UUID + " = ? ",
                new String[]{uuidString});
    }

    private ScanItemCursorWrapper queryScanItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ScanItemTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ScanItemCursorWrapper(cursor);
    }

    public List<ScanItem> getScanItems() {
        List<ScanItem> scanItems = new ArrayList<>();

        ScanItemCursorWrapper cursor = queryScanItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                scanItems.add(cursor.getScanItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return scanItems;
    }

    public void addScanItem(ScanItem scanItem) {
        ContentValues values = getContentValues(scanItem);

        mDatabase.insert(ScanItemTable.NAME, null, values);
    }

    // todo: use in future
    /*
    public File getPhotoFile(ScanItem ScanItem) {
        File externalFileDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(externalFileDir == null)
            return null;

        return new File(externalFileDir, ScanItem.getPhotoFilename());
    }*/

    // todo: use to clear history
    public void deleteScanItem(ScanItem scanItem) {
        mDatabase.delete(ScanItemTable.NAME, ScanItemTable.Cols.UUID + " = ?",
                new String[]{scanItem.getId().toString()});
    }

    public ScanItem getScanItem(UUID id) {
        ScanItemCursorWrapper cursor = queryScanItems(
                ScanItemTable.Cols.UUID + " = ? ",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getScanItem();
        } finally {
            cursor.close();
        }
    }
}
