package com.kfr2359.homefinancemobile.storage;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MoneyOpsLocalStorageTest {
    @Test
    public void isInitValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);

        Cursor c = storage.getReadableDatabase().query(FolderDAOImpl.TABLE, null, null, null,
                                                        null, null, null);
        int numRows = 0;
        while (c.moveToNext()) {
            numRows++;
        }

        Assert.assertEquals(numRows, 1);
    }

    @Test
    public void isRootFolderValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        FolderDAOImpl folderDAO = new FolderDAOImpl(storage);

        Folder root = folderDAO.getById(1);

        Assert.assertEquals(root.getName(), "Операции");
        Assert.assertEquals(root.getParent(), null);
    }

}
