package com.kfr2359.homefinancemobile.storage.DAO;

import com.kfr2359.homefinancemobile.logic.GenericEntity;

import java.util.List;

public interface GenericDAO <E extends GenericEntity>{
    List<E> getAll() throws DAOException;
    List<E> getByParent(GenericEntity parent) throws DAOException;
    E getById(Integer id) throws DAOException;
    void update(E entity) throws DAOException;
    void delete(E entity) throws DAOException;
    void create(E entity) throws DAOException;

    class DAOException extends Exception {
        public DAOException() {
        }

        public DAOException(String message) {
            super(message);
        }
    }
}
