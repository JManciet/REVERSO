package dao;

import exceptions.CustomException;
import exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {

    List<T> findAll() throws Exception;

    T findByName(String nom) throws Exception;

    void create(T entity) throws Exception;

    void update(T entity) throws Exception;

    void delete(T entity) throws Exception;

}
