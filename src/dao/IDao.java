package dao;

import exceptions.CustomException;
import exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {

    List<T> findAll() throws DaoException, CustomException;

    T findByName(String nom) throws DaoException, CustomException;

    void create(T entity) throws DaoException, CustomException;

    void update(T entity) throws DaoException, CustomException;

    void delete(T entity) throws DaoException;

}
