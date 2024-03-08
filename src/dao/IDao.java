package dao;

import entites.Societe;

import java.util.ArrayList;

public interface IDao<T> {

    ArrayList<Societe> findAll() throws Exception;

    T findByName(String nom) throws Exception;

    void create(T entity) throws Exception;

    void update(T entity) throws Exception;

    void delete(T entity) throws Exception;

}
