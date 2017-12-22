package com.kfr2359.homefinancemobile.logic;

public class Item  extends GenericEntity {
    private String name;
    private Folder folder;

    public Item(Integer id, String name, Folder folder) {
        super(id);
        this.id = id;
        this.name = name;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return name;
    }
}
