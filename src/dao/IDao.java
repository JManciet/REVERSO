package dao;

import exceptions.DaoException;

import java.util.List;

public interface IDao<T> {

    List<T> findAll();

    T findByName(String nom);

    void create(T entity) throws DaoException;

    void update(T entity) throws DaoException;

    void delete(T entity);

}
