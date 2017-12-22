package com.kfr2359.homefinancemobile.storage.LocalStorageImpl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kfr2359.homefinancemobile.logic.Item;
import com.kfr2359.homefinancemobile.logic.MoneyOp;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.DAO.ItemDAO;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.ItemDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.MoneyOpDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.MoneyOpDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class MoneyOpDAOTest {
    @Test
    public void isCreateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);
        MoneyOpDAO moneyOpDAO = new MoneyOpDAOImpl(storage);
        Date date = new Date(0);
        Item inItem = new Item(1, "testItem", folderDAO.getById(1));
        itemDAO.create(inItem);

        MoneyOp inOp = new MoneyOp(1, 10.0f, "testOp", date, inItem);
        moneyOpDAO.create(inOp);
        MoneyOp outOp = moneyOpDAO.getById(1);

        Assert.assertNotEquals(outOp, null);
        Assert.assertEquals(inOp.getDesciption(), outOp.getDesciption());
        Assert.assertTrue(inOp.getAmount() - outOp.getAmount() < 0.01f);
        Assert.assertEquals(inOp.getOpDate(), outOp.getOpDate());
        Assert.assertEquals(inOp.getItem().getId(), outOp.getItem().getId());
    }

    @Test
    public void isUpdateValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);
        MoneyOpDAO moneyOpDAO = new MoneyOpDAOImpl(storage);
        Date date = new Date(0);
        Item inItem = new Item(1, "testItem", folderDAO.getById(1));
        itemDAO.create(inItem);

        MoneyOp inOp = new MoneyOp(1, 10.0f, "testOp", date, inItem);
        moneyOpDAO.create(inOp);
        inOp.setDesciption("testOp");
        moneyOpDAO.update(inOp);
        MoneyOp outOp = moneyOpDAO.getById(1);

        Assert.assertEquals(outOp.getDesciption(), "testOp");
    }

    @Test
    public void isDeleteValid() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(appContext);
        storage.onUpgrade(storage.getWritableDatabase(), 1, 1);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        FolderDAO folderDAO = new FolderDAOImpl(storage);
        MoneyOpDAO moneyOpDAO = new MoneyOpDAOImpl(storage);
        Date date = new Date(0);
        Item inItem = new Item(1, "test", folderDAO.getById(1));
        itemDAO.create(inItem);

        MoneyOp inOp = new MoneyOp(1, 10.0f, "testOp", date, inItem);
        moneyOpDAO.create(inOp);
        moneyOpDAO.delete(inOp);
        MoneyOp outOp = moneyOpDAO.getById(1);

        Assert.assertEquals(outOp, null);
    }
}
