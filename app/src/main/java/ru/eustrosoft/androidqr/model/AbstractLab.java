package ru.eustrosoft.androidqr.model;

// TODO: use in future
/*
public abstract class AbstractLab<T> {
    private static AbstractLab abstractLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AbstractLab(Context context) {
        mContext = context.getApplicationContext();
    }

    private static ContentValues getContentValues(AbstractModel AbstractModel) {
        ContentValues values = new ContentValues();

        return values;
    }

    
    public void updateAbstractModel(AbstractModel AbstractModel) {
    }

    private AbstractModelCursorWrapper queryAbstractModels(String whereClause, String[] whereArgs) {
        return null;
    }

    public List<AbstractModel> getModel() {
        List<AbstractModel> AbstractModels = new ArrayList<>();

        AbstractModelCursorWrapper cursor = queryAbstractModels(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AbstractModels.add(cursor.getAbstractModel());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return AbstractModels;
    }

    public void addAbstractModel(AbstractModel AbstractModel) {
        ContentValues values = getContentValues(AbstractModel);

        mDatabase.insert(AbstractModelDBSchema.AbstractModelTable.NAME, null, values);
    }
    
    
    public void deleteAbstractModel(AbstractModel AbstractModel) {
        mDatabase.delete(AbstractModelDBSchema.AbstractModelTable.NAME, AbstractModelDBSchema.AbstractModelTable.Cols.UUID + " = ?",
                new String[]{AbstractModel.getId().toString()});
    }

    public AbstractModel getAbstractModel(UUID id) {
        AbstractModelCursorWrapper cursor = queryAbstractModels(
                AbstractModelDBSchema.AbstractModelTable.Cols.UUID + " = ? ",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getAbstractModel();
        } finally {
            cursor.close();
        }
    }
}*/
