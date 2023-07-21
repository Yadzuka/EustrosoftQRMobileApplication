package ru.eustrosoft.androidqr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.eustrosoft.androidqr.database.ProfileBaseHelper;
import ru.eustrosoft.androidqr.database.ProfileCursorWrapper;
import ru.eustrosoft.androidqr.database.ProfileDBSchema.ProfileTable;

public class ProfileDAO {
    private static ProfileDAO profileDAO;

    private Context context;
    private SQLiteDatabase mDatabase;

    private ProfileDAO(Context context) {
        this.context = context.getApplicationContext();
        mDatabase = new ProfileBaseHelper(this.context)
                .getWritableDatabase();
    }

    public static ProfileDAO get(Context context) {
        if (profileDAO == null)
            profileDAO = new ProfileDAO(context);

        return profileDAO;
    }

    private static ContentValues getContentValues(Profile profile) {
        ContentValues values = new ContentValues();
        values.put(ProfileTable.Cols.UUID, profile.getId().toString());
        values.put(ProfileTable.Cols.DATE, profile.getDate().getTime());
        values.put(ProfileTable.Cols.NAME, profile.getName());
        values.put(ProfileTable.Cols.PASSWORD, profile.getPassword());

        return values;
    }

    // todo: remove
    public void updateProfile(Profile profile) {
        String uuidString = profile.getId().toString();
        ContentValues values = getContentValues(profile);

        mDatabase.update(ProfileTable.NAME, values,
                ProfileTable.Cols.UUID + " = ? ",
                new String[]{uuidString});
    }

    private ProfileCursorWrapper queryProfiles(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProfileTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ProfileCursorWrapper(cursor);
    }

    public List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<>();

        ProfileCursorWrapper cursor = queryProfiles(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                profiles.add(cursor.getProfile());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return profiles;
    }

    public void addProfile(Profile Profile) {
        ContentValues values = getContentValues(Profile);

        mDatabase.insert(ProfileTable.NAME, null, values);
    }

    public void deleteProfile(Profile Profile) {
        mDatabase.delete(ProfileTable.NAME, ProfileTable.Cols.UUID + " = ?",
                new String[]{Profile.getId().toString()});
    }

    public Profile getProfile(UUID id) {
        ProfileCursorWrapper cursor = queryProfiles(
                ProfileTable.Cols.UUID + " = ? ",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getProfile();
        } finally {
            cursor.close();
        }
    }

    public Profile getProfileByName(String name) {
        ProfileCursorWrapper cursor = queryProfiles(
                ProfileTable.Cols.NAME + " = ? ",
                new String[]{name}
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getProfile();
        } finally {
            cursor.close();
        }
    }
}
