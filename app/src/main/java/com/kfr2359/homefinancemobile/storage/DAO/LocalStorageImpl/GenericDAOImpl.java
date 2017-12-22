package com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kfr2359.homefinancemobile.logic.GenericEntity;
import com.kfr2359.homefinancemobile.storage.DAO.GenericDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAOImpl<E extends GenericEntity> implements GenericDAO<E> {
    protected String tableName = "";
    protected String idColumnName = "";
    protected MoneyOpsLocalStorage storage;

    protected abstract E getByCursorInCurPos(Cursor cursor) throws DAOException;
    protected abstract ContentValues getContentValues(E entity);
    protected abstract String [] getColumnNames();

    public GenericDAOImpl(String tableName, String idColumnName, MoneyOpsLocalStorage storage) {
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.storage = storage;
    }

    @Override
    public List getAll() throws DAOException {
        List<E> result = new ArrayList<E>();
        SQLiteDatabase db = storage.getReadableDatabase();

        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getByCursorInCurPos(cursor));
        }

        return result;
    }

    @Override
    public E getById(Integer id) throws DAOException {
        SQLiteDatabase db = storage.getReadableDatabase();

        Cursor cursor = db.query(tableName, getColumnNames(), idColumnName + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return getByCursorInCurPos(cursor);
    }

    @Override
    public void update(E entity) throws DAOException {
        SQLiteDatabase db = storage.getWritableDatabase();

        ContentValues values = getContentValues(entity);
        int numAffectedRows = db.update(tableName, values, idColumnName+"=?", new String[]{String.valueOf(entity.getId())});
        if (numAffectedRows != 1) {
            throw new DAOException("update, affected rows: " + String.valueOf(numAffectedRows));
        }
    }

    @Override
    public void delete(E entity) throws DAOException {
        SQLiteDatabase db = storage.getWritableDatabase();

        int numAffectedRows = db.delete(tableName, idColumnName+"=?", new String[]{String.valueOf(entity.getId())});
        if (numAffectedRows != 1) {
            throw new DAOException("delete, affected rows: " + String.valueOf(numAffectedRows));
        }
    }

    @Override
    public void create(E entity)  throws DAOException {
        SQLiteDatabase db = storage.getWritableDatabase();

        ContentValues values = getContentValues(entity);
        long res = db.insert(tableName, null, values);

        if (res == -1) {
            throw new DAOException("create, result: " + String.valueOf(res));
        }
    }
}
