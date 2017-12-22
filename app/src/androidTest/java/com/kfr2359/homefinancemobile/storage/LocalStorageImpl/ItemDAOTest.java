package com.kfr2359.homefinancemobile.storage.LocalStorageImpl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kfr2359.homefinancemobile.logic.Item;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.DAO.ItemDAO;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.ItemDAOImpl;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ItemDAOTest {
    @Test
    public void isCreateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Item inItem = new Item(1, "testItem", folderDAO.getById(1));
        itemDAO.create(inItem);
        Item outItem = itemDAO.getById(1);

        Assert.assertNotEquals(outItem, null);
        Assert.assertEquals(inItem.getName(), outItem.getName());
        Assert.assertEquals(inItem.getFolder().getId(), outItem.getFolder().getId());
    }

    @Test
    public void isUpdateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Item inItem = new Item(1, "testItem", folderDAO.getById(1));
        itemDAO.create(inItem);
        inItem.setName("testItem2");
        itemDAO.update(inItem);
        Item outItem = itemDAO.getById(1);

        Assert.assertEquals(outItem.getName(), "testItem2");
    }

    @Test
    public void isDeleteValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);

        Item inItem = new Item(1, "test", folderDAO.getById(1));
        itemDAO.create(inItem);
        itemDAO.delete(inItem);
        Item outItem = itemDAO.getById(1);

        Assert.assertEquals(outItem, null);
    }
}
