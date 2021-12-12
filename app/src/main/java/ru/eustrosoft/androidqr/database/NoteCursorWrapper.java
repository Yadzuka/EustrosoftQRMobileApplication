package ru.eustrosoft.androidqr.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.eustrosoft.androidqr.database.NoteDBSchema.NoteTable;
import ru.eustrosoft.androidqr.model.Note;

public class NoteCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        long time = getLong(getColumnIndex(NoteTable.Cols.TIME));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String text = getString(getColumnIndex(NoteTable.Cols.TEXT));

        Note item = new Note(UUID.fromString(uuidString));
        item.setDate(new Date(date));
        item.setTime(new Date(time));
        item.setText(text);
        item.setTitle(title);

        return item;
    }
}
