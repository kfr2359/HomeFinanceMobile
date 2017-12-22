package com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kfr2359.homefinancemobile.MainActivity;
import com.kfr2359.homefinancemobile.logic.GenericEntity;
import com.kfr2359.homefinancemobile.logic.Item;
import com.kfr2359.homefinancemobile.logic.MoneyOp;
import com.kfr2359.homefinancemobile.storage.DAO.ItemDAO;
import com.kfr2359.homefinancemobile.storage.DAO.MoneyOpDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MoneyOpDAOImpl extends GenericDAOImpl<MoneyOp> implements MoneyOpDAO {
    public static final String TABLE = "money_op";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_OP_DATE = "op_date";
    public static final String COLUMN_ITEM = "item_id";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS money_op (" +
                    "    id          INTEGER PRIMARY KEY NOT NULL," +
                    "    amount      REAL                NOT NULL," +
                    "    description TEXT                NOT NULL," +
                    "    op_date     TEXT                NOT NULL," +
                    "    item_id     INTEGER             NOT NULL," +
                    "    FOREIGN KEY (item_id) REFERENCES item(id)" +
                    "    ON UPDATE CASCADE ON DELETE CASCADE"       +
                    ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS money_op";

    private ItemDAO itemDAO;
    private DateFormat df;

    public MoneyOpDAOImpl(MoneyOpsLocalStorage storage) {
        super(TABLE, COLUMN_ID, storage);
        itemDAO = new ItemDAOImpl(storage);
        df = new SimpleDateFormat(MainActivity.DATEFORMAT_PATTERN);
    }

    @Override
    public List<MoneyOp> getByParent(GenericEntity parent) throws DAOException {
        List<MoneyOp> result = new ArrayList<MoneyOp>();
        SQLiteDatabase db = storage.getReadableDatabase();

        Cursor cursor = db.query(tableName, getColumnNames(), COLUMN_ITEM+"=?", new String[]{String.valueOf(parent.getId())},
                null, null, null);

        while (cursor.moveToNext()) {
            result.add(getByCursorInCurPos(cursor));
        }

        return result;
    }

    @Override
    protected MoneyOp getByCursorInCurPos(Cursor cursor) {
        int idColIdx = cursor.getColumnIndex(COLUMN_ID);
        int amountColIdx = cursor.getColumnIndex(COLUMN_AMOUNT);
        int desciptionColIdx = cursor.getColumnIndex(COLUMN_DESCRIPTION);
        int opDateColIdx = cursor.getColumnIndex(COLUMN_OP_DATE);
        int itemColIdx = cursor.getColumnIndex(COLUMN_ITEM);

        MoneyOp result = null;
        try {
            result = new MoneyOp(cursor.getInt(idColIdx), cursor.getFloat(amountColIdx),
                    cursor.getString(desciptionColIdx), df.parse(cursor.getString(opDateColIdx)),
                    itemDAO.getById(cursor.getInt(itemColIdx)));
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    @Override
    protected ContentValues getContentValues(MoneyOp entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, entity.getAmount());
        contentValues.put(COLUMN_DESCRIPTION, entity.getDesciption());
        contentValues.put(COLUMN_OP_DATE, df.format(entity.getOpDate()));
        contentValues.put(COLUMN_ITEM, entity.getItem().getId());

        return contentValues;
    }

    @Override
    protected String[] getColumnNames() {
        return new String[] {COLUMN_ID, COLUMN_AMOUNT, COLUMN_DESCRIPTION, COLUMN_OP_DATE, COLUMN_ITEM};
    }
}
