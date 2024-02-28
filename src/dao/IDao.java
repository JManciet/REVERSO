package dao;

import java.util.List;

public interface IDao<T> {

    List<T> findAll();

    T findByName(String nom);

    void create(T entity);

    void update(T entity);

    void delete(T entity);

}
