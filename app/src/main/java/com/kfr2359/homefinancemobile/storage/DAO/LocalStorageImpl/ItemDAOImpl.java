package com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.logic.GenericEntity;
import com.kfr2359.homefinancemobile.logic.Item;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.DAO.ItemDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl extends GenericDAOImpl<Item> implements ItemDAO{
    public static final String TABLE = "item";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FOLDER = "folder_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS item (" +
                    "    id          INTEGER PRIMARY KEY NOT NULL,"      +
                    "    name        TEXT                NOT NULL,"      +
                    "    folder_id   INTEGER,"                           +
                    "    FOREIGN KEY (folder_id) REFERENCES folder (id)" +
                    "    ON UPDATE CASCADE ON DELETE CASCADE"            +
                    ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS item";

    private FolderDAO folderDAO;

    public ItemDAOImpl(MoneyOpsLocalStorage storage) {
        super(TABLE, COLUMN_ID, storage);
        folderDAO = new FolderDAOImpl(storage);
    }

    @Override
    protected Item getByCursorInCurPos(Cursor cursor) throws DAOException {
        int idColIdx = cursor.getColumnIndex(COLUMN_ID);
        int nameColIdx = cursor.getColumnIndex(COLUMN_NAME);
        int folderColIdx = cursor.getColumnIndex(COLUMN_FOLDER);


        Item result = new Item(cursor.getInt(idColIdx), cursor.getString(nameColIdx), folderDAO.getById(cursor.getInt(folderColIdx)));
        return result;
    }

    @Override
    protected ContentValues getContentValues(Item entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, entity.getName());
        contentValues.put(COLUMN_FOLDER, entity.getFolder().getId());

        return contentValues;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_FOLDER};
    }

    @Override
    public List<Item> getByParent(GenericEntity parent) throws DAOException {
        List<Item> result = new ArrayList<Item>();
        SQLiteDatabase db = storage.getReadableDatabase();

        Cursor cursor = db.query(tableName, getColumnNames(), COLUMN_FOLDER+"=?", new String[]{String.valueOf(parent.getId())},
                null, null, null);

        while (cursor.moveToNext()) {
            result.add(getByCursorInCurPos(cursor));
        }

        return result;
    }
}
