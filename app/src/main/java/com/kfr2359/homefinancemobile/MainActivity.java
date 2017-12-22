package com.kfr2359.homefinancemobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.logic.Item;
import com.kfr2359.homefinancemobile.logic.MoneyOp;
import com.kfr2359.homefinancemobile.storage.DAO.FolderDAO;
import com.kfr2359.homefinancemobile.storage.DAO.GenericDAO;
import com.kfr2359.homefinancemobile.storage.DAO.ItemDAO;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.FolderDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.ItemDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.LocalStorageImpl.MoneyOpDAOImpl;
import com.kfr2359.homefinancemobile.storage.DAO.MoneyOpDAO;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;
import com.kfr2359.homefinancemobile.storage.TreeToLocalStorageSynchronizer;
import com.kfr2359.homefinancemobile.view.MoneyOpNodeViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Date;

public class MainActivity extends Activity {
    public static final String DATEFORMAT_PATTERN = "dd-MM-yyyy HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoneyOpsLocalStorage storage = new MoneyOpsLocalStorage(this);
        storage.onUpgrade(storage.getWritableDatabase(),1, 1);
        TreeNode model = TreeNode.root();

        AndroidTreeView treeView = new AndroidTreeView(this, model);
        treeView.setDefaultContainerStyle(R.style.TreeNodeStyle);
        treeView.setDefaultViewHolder(MoneyOpNodeViewHolder.class);

        TreeToLocalStorageSynchronizer synchronizer = new TreeToLocalStorageSynchronizer(model, storage);
        synchronizer.recreateModel();


        FolderDAO folderDAO = new FolderDAOImpl(storage);
        ItemDAO itemDAO = new ItemDAOImpl(storage);
        MoneyOpDAO moneyOpDAO = new MoneyOpDAOImpl(storage);

        Folder inFolder = null;
        Item inItem = null;
        MoneyOp inOp = null;

        Date date = new Date(0);
        try {
            inFolder = new Folder(2, "ggg", folderDAO.getById(1));
            folderDAO.create(inFolder);
            inItem = new Item(1, "testItem", folderDAO.getById(2));
            itemDAO.create(inItem);
            inOp = new MoneyOp(1, 10.0f, "testOp", date, inItem);
            moneyOpDAO.create(inOp);
        } catch (GenericDAO.DAOException e) {
            e.printStackTrace();
        }
        TreeNode nodeFolder = new TreeNode(inFolder);
        model.addChild(nodeFolder);
        TreeNode nodeItem = new TreeNode(inItem);
        nodeFolder.addChild(nodeItem);

        nodeItem.addChild(new TreeNode(inOp));

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main);
        View view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(view);
    }

}
