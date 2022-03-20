package ru.eustrosoft.androidqr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.database.NoteBaseHelper;
import ru.eustrosoft.androidqr.database.NoteCursorWrapper;
import ru.eustrosoft.androidqr.database.NoteDBSchema;

public class NoteLab {
    private static NoteLab sNoteLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private NoteLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static NoteLab get(Context context) {
        if (sNoteLab == null)
            sNoteLab = new NoteLab(context);

        return sNoteLab;
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDBSchema.NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteDBSchema.NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteDBSchema.NoteTable.Cols.TIME, note.getTime().getTime());
        values.put(NoteDBSchema.NoteTable.Cols.TEXT, note.getText());
        values.put(NoteDBSchema.NoteTable.Cols.TITLE, note.getTitle());

        return values;
    }

    public void updateScanItem(Note note) {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);

        mDatabase.update(NoteDBSchema.NoteTable.NAME, values,
                NoteDBSchema.NoteTable.Cols.UUID + " = ? ",
                new String[]{uuidString});
    }

    public File getPhotoFile(Note note) {
        File externalFileDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFileDir == null)
            return null;

        return new File(externalFileDir, note.getPhotoFilename());
    }

    private NoteCursorWrapper queryScanItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteDBSchema.NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new NoteCursorWrapper(cursor);
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();

        NoteCursorWrapper cursor = queryScanItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteDBSchema.NoteTable.NAME, null, values);
    }

    public void deleteNote(Note note) {
        mDatabase.delete(
                NoteDBSchema.NoteTable.NAME,
                NoteDBSchema.NoteTable.Cols.UUID + " = ?",
                new String[]{note.getId().toString()}
        );
    }

    public Note getNote(UUID id) {
        NoteCursorWrapper cursor = queryScanItems(
                NoteDBSchema.NoteTable.Cols.UUID + " = ? ",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }
}
