package com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.logic.GenericEntity;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import java.util.ArrayList;
import java.util.List;

public class FolderDAOImpl extends GenericDAOImpl<Folder> implements FolderDAO {
    public static final String TABLE = "folder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PARENT = "parent_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS folder (" +
                    "    id          INTEGER PRIMARY KEY NOT NULL,"      +
                    "    name        TEXT                NOT NULL,"      +
                    "    parent_id   INTEGER,"                           +
                    "    FOREIGN KEY (parent_id) REFERENCES folder (id)" +
                    "    ON UPDATE CASCADE ON DELETE CASCADE"            +
                    ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS folder";

    public FolderDAOImpl(MoneyOpsLocalStorage storage) {
        super(TABLE, COLUMN_ID, storage);
    }

    @Override
    public List<Folder> getByParent(GenericEntity parent) throws DAOException {
        List<Folder> result = new ArrayList<Folder>();
        SQLiteDatabase db = storage.getReadableDatabase();

        Cursor cursor = db.query(tableName, getColumnNames(), COLUMN_PARENT+"=?", new String[]{String.valueOf(parent.getId())},
                                null, null, null);

        while (cursor.moveToNext()) {
            result.add(getByCursorInCurPos(cursor));
        }

        return result;
    }

    @Override
    protected Folder getByCursorInCurPos(Cursor cursor) throws DAOException {
        int idColIdx = cursor.getColumnIndex(COLUMN_ID);
        int nameColIdx = cursor.getColumnIndex(COLUMN_NAME);
        int parentColIdx = cursor.getColumnIndex(COLUMN_PARENT);

        Folder result = new Folder(cursor.getInt(idColIdx), cursor.getString(nameColIdx), getById(cursor.getInt(parentColIdx)));
        return result;
    }

    @Override
    protected ContentValues getContentValues(Folder entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, entity.getName());
        contentValues.put(COLUMN_PARENT, entity.getParent().getId());

        return contentValues;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_PARENT};
    }

}
