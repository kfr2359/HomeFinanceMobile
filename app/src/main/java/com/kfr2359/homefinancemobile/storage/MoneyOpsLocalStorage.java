package com.kfr2359.homefinancemobile.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.ItemDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.MoneyOpDAOImpl;

public class MoneyOpsLocalStorage extends SQLiteOpenHelper {
    private static final String DB_NAME = "MoneyOpsDB";
    private static final int DB_VERSION = 1;

    private static final String INSERT_ROOT_FOLDER = "INSERT INTO folder (name, parent_id) VALUES (\"Операции\", NULL)";

    public MoneyOpsLocalStorage(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FolderDAOImpl.CREATE_TABLE);
        db.execSQL(ItemDAOImpl.CREATE_TABLE);
        db.execSQL(MoneyOpDAOImpl.CREATE_TABLE);
        db.execSQL(INSERT_ROOT_FOLDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(FolderDAOImpl.DROP_TABLE);
        db.execSQL(ItemDAOImpl.DROP_TABLE);
        db.execSQL(MoneyOpDAOImpl.DROP_TABLE);

        onCreate(db);
    }
}
