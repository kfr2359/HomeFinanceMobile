package com.kfr2359.homefinancemobile.storage.LocalStorageImpl;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FolderDAOTest {
    @Test
    public void isCreateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Folder inFldr = new Folder(2, "test", folderDAO.getById(1));
        folderDAO.create(inFldr);
        Folder outFldr = folderDAO.getById(2);

        Assert.assertNotEquals(outFldr, null);
        Assert.assertEquals(inFldr.getName(), outFldr.getName());
        Assert.assertEquals(inFldr.getParent().getId(), outFldr.getParent().getId());
    }

    @Test
    public void isUpdateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Folder inFldr = new Folder(2, "test", folderDAO.getById(1));
        folderDAO.create(inFldr);
        inFldr.setName("test2");
        folderDAO.update(inFldr);
        Folder outFldr = folderDAO.getById(2);

        Assert.assertEquals(outFldr.getName(), "test2");
    }

    @Test
    public void isDeleteValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Folder inFldr = new Folder(2, "test", folderDAO.getById(1));
        folderDAO.create(inFldr);
        folderDAO.delete(inFldr);
        Folder outFldr = folderDAO.getById(2);

        Assert.assertEquals(outFldr, null);
    }
}
