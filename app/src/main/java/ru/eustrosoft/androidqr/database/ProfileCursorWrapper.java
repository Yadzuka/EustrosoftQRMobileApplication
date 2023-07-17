package ru.eustrosoft.androidqr.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.eustrosoft.androidqr.model.Profile;

public class ProfileCursorWrapper extends CursorWrapper {

    public ProfileCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Profile getProfile() {
        String uuidString = getString(getColumnIndex(ProfileDBSchema.ProfileTable.Cols.UUID));
        long date = getLong(getColumnIndex(ProfileDBSchema.ProfileTable.Cols.DATE));
        String name = getString(getColumnIndex(ProfileDBSchema.ProfileTable.Cols.NAME));
        String password = getString(getColumnIndex(ProfileDBSchema.ProfileTable.Cols.PASSWORD));

        Profile item = new Profile(UUID.fromString(uuidString));
        item.setDate(new Date(date));
        item.setName(name);
        item.setPassword(password);
        return item;
    }
}
