package com.kfr2359.homefinancemobile.logic;

public abstract class GenericEntity {
    protected Integer id;

    public GenericEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract String toString();

}
