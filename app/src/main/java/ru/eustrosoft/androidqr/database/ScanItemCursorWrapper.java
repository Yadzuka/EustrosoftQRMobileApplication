package ru.eustrosoft.androidqr.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.eustrosoft.androidqr.database.ScanItemDBSchema.ScanItemTable;
import ru.eustrosoft.androidqr.model.ScanItem;

public class ScanItemCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ScanItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ScanItem getScanItem() {
        String uuidString = getString(getColumnIndex(ScanItemTable.Cols.UUID));
        long date = getLong(getColumnIndex(ScanItemTable.Cols.DATE));
        long time = getLong(getColumnIndex(ScanItemTable.Cols.TIME));
        String text = getString(getColumnIndex(ScanItemTable.Cols.TEXT));

        ScanItem item = new ScanItem(UUID.fromString(uuidString));
        item.setDate(new Date(date));
        item.setTime(new Date(time));
        item.setText(text);

        return item;
    }
}
