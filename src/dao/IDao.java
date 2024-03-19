package dao;

import entites.Societe;

import java.util.ArrayList;

/**
 * Interface générique définissant les méthodes de base d'une DAO.
 *
 * @param <T>
 */
public interface IDao<T> {

    /**
     * Récupère toutes les entités.
     *
     * @return Une liste contenant toutes les sociétés trouvées
     * @throws Exception
     */
    ArrayList<Societe> findAll() throws Exception;

    /**
     * Recherche une entité par son nom.
     *
     * @param nom Le nom de l'entité à rechercher
     * @return L'entité trouvée
     * @throws Exception
     */
    T findByName(String nom) throws Exception;

    /**
     * Crée une nouvelle entité.
     *
     * @param entity L'entité à créer
     * @throws Exception
     */
    void create(T entity) throws Exception;

    /**
     * Met à jour une entité existante.
     *
     * @param entity L'entité à mettre à jour
     * @throws Exception
     */
    void update(T entity) throws Exception;

    /**
     * Supprime une entité existante.
     *
     * @param entity L'entité à supprimer
     * @throws Exception
     */
    void delete(T entity) throws Exception;
}
