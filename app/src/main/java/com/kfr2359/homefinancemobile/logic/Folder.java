package com.kfr2359.homefinancemobile.logic;

public class Folder extends GenericEntity {
    private Integer id;
    private String name;
    private Folder parent;

    public Folder(Integer id, String name, Folder parent) {
        super(id);
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return name;
    }
}
