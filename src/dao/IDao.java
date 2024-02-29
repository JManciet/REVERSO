package dao;

import exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {

    List<T> findAll() throws DaoException, SQLException;

    T findByName(String nom) throws DaoException, SQLException;

    void create(T entity) throws DaoException, SQLException;

    void update(T entity) throws DaoException, SQLException;

    void delete(T entity) throws DaoException, SQLException;

}
