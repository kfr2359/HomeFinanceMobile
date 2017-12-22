package com.kfr2359.homefinancemobile.storage;

import com.kfr2359.homefinancemobile.logic.Folder;
import com.kfr2359.homefinancemobile.logic.GenericEntity;
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
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TreeToLocalStorageSynchronizer {
    private FolderDAO folderDAO;
    private ItemDAO itemDAO;
    private MoneyOpDAO moneyOpDAO;

    private MoneyOpsLocalStorage localStorage;
    private TreeNode modelRoot;
    private Map<GenericEntity, TreeNode> entityToNodeMap;

    public TreeToLocalStorageSynchronizer(TreeNode modelRoot, MoneyOpsLocalStorage localStorage) {
        this.modelRoot = modelRoot;
        this.localStorage = localStorage;

        folderDAO = new FolderDAOImpl(localStorage);
        itemDAO = new ItemDAOImpl(localStorage);
        moneyOpDAO = new MoneyOpDAOImpl(localStorage);
        entityToNodeMap = new HashMap<GenericEntity, TreeNode>();
    }

    public TreeNode getModel() {
        return modelRoot;
    }

    public void recreateModel() {
        resetModel();
        fillModel();
    }

    private void resetModel() {
        Iterator<TreeNode> it = modelRoot.getChildren().iterator();
        while (it.hasNext()) {
            TreeNode node = it.next();
            modelRoot.deleteChild(node);
        }

        entityToNodeMap.clear();
    }

    private void fillModel() {
        //assuming root Folder at id=1
        Folder root;
        try {
            root = folderDAO.getById(1);
            doFillBFS(root);
        } catch (GenericDAO.DAOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void doFillBFS(GenericEntity root) throws GenericDAO.DAOException{
        Queue<GenericEntity> queue = new ArrayDeque<GenericEntity>();

        queue.add(root);
        entityToNodeMap.put(root, modelRoot);

        while (!queue.isEmpty()) {
            GenericEntity entity = queue.poll();
            TreeNode entityNode = entityToNodeMap.get(entity);

            Iterator<GenericEntity> it = getChildrenFromStorage(entity).iterator();
            while (it.hasNext()) {
                GenericEntity childEntity = it.next();
                TreeNode childNode = new TreeNode(childEntity);

                entityNode.addChild(childNode);
                queue.add(childEntity);
                entityToNodeMap.put(childEntity, childNode);
            }
        }
    }

    private List<GenericEntity> getChildrenFromStorage(GenericEntity parent) throws GenericDAO.DAOException{
        List<GenericEntity> children = new ArrayList<GenericEntity>();

        List<Folder> folderChildren = folderDAO.getByParent(parent);
        List<Item> itemChildren = itemDAO.getByParent(parent);
        List<MoneyOp> moneyOpChildren = moneyOpDAO.getByParent(parent);

        children.addAll(folderChildren);
        children.addAll(itemChildren);
        children.addAll(moneyOpChildren);

        return children;
    }

    public void recreateStorage() {
        resetStorage();
        fillStorage();
    }

    private void resetStorage() {
        localStorage.onUpgrade(localStorage.getWritableDatabase(), 1, 1);
    }

    private void fillStorage() {
        try {
            Queue<TreeNode> queue = new ArrayDeque<TreeNode>();

            queue.add(modelRoot);

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();

                GenericDAO dao = getAppropriateDAO((GenericEntity) node.getValue());
                dao.create((GenericEntity) node.getValue());

                Iterator<TreeNode> it = node.getChildren().iterator();
                while (it.hasNext()) {
                    TreeNode childNode = it.next();
                    queue.add(childNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            localStorage.close();
        }
    }

    private GenericDAO getAppropriateDAO(GenericEntity entity) throws GenericDAO.DAOException {
        Class nodeValType = entity.getClass();

        if (nodeValType == Folder.class) {
            return folderDAO;
        } else if (nodeValType == Item.class) {
            return itemDAO;
        } else if (nodeValType == MoneyOp.class) {
            return moneyOpDAO;
        } else {
            throw new GenericDAO.DAOException("Unknown node type:" + nodeValType.getName());
        }
    }

    public void addChild(TreeNode parent, TreeNode child) {
        parent.addChild(child);

        try {
            GenericDAO dao = getAppropriateDAO((GenericEntity) child.getValue());
            dao.create((GenericEntity) child.getValue());
        } catch (GenericDAO.DAOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteChild(TreeNode parent, TreeNode child) {
        parent.deleteChild(child);

        try {
            GenericDAO dao = getAppropriateDAO((GenericEntity) child.getValue());
            dao.delete((GenericEntity) child.getValue());
        } catch (GenericDAO.DAOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateToStorage(TreeNode node) {
        try {
            GenericDAO dao = getAppropriateDAO((GenericEntity) node.getValue());
            dao.update((GenericEntity) node.getValue());
        } catch (GenericDAO.DAOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
