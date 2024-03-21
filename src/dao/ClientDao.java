package dao;

import entites.Adresse;
import entites.Client;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Gravite;
import utilitaires.Utilitaires;

import java.sql.*;
import java.util.ArrayList;

import static utilitaires.Utilitaires.LOGGER;

/**
 * Classe implémentant l'interface `IDao` pour les clients.
 * Permet de gérer les opérations CRUD (création, lecture, mise à jour,
 * suppression) sur les clients dans la base de données.
 */
public class ClientDao implements IDao<Client>{

    /**
     * Méthode pour récupérer tous les clients de la base de données.
     *
     * @return Une liste de clients
     * @throws Exception
     */
    @Override
    public ArrayList<Societe> findAll() throws Exception {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
            statement = connection.prepareStatement(
                     "SELECT c.*, a.* FROM CLIENT c " +
                             "LEFT JOIN ADRESSE a ON c.IDADRESSE = a" +
                             ".IDADRESSE");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Societe> clients = new ArrayList<>();
            while (resultSet.next()) {

                Adresse adresse = new Adresse(
                        resultSet.getInt("IDADRESSE"),
                        resultSet.getString("NUMERORUE"),
                        resultSet.getString("NOMRUE"),
                        resultSet.getString("CODEPOSTAL"),
                        resultSet.getString("VILLE")
                );

                clients.add(new Client(
                        resultSet.getInt("IDCLIENT"),
                        resultSet.getString("RAISONSOCIALECLIENT"),
                        adresse,
                        resultSet.getString("TELEPHONECLIENT"),
                        resultSet.getString("EMAILCLIENT"),
                        resultSet.getString("COMMENTAIRESCLIENT"),
                        resultSet.getDouble("CHIFFREAFFAIRE"),
                        resultSet.getInt("NBREMPLOYES")
                ));
            }
            return clients;
        } catch (CustomException ce) {
            LOGGER.severe("Une donnée de la BDD n'est pas conforme : "+ ce);
            throw new DaoException(Gravite.SEVERE,"Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter " +
                    "un administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche des " +
                    "clients dans la base de donnée : "+ sqle);
            throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                    "recherche des clients dans la base de donnée. " +
                    "Veuillez contacter un administrateur.\nFermeture de l'application.");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche des " +
                        "clients dans la base de donnée.: " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de " +
                        "la " +
                        "recherche des clients dans la base de donnée. " +
                        "Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour rechercher un client par son nom.
     *
     * @param nom Le nom du client à rechercher
     * @return Le client trouvé ou null si aucun client n'est trouvé
     * @throws Exception
     */
    @Override
    public Client findByName(String nom) throws Exception {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
             statement = connection.prepareStatement(
                     "SELECT c.*, a.* FROM CLIENT c " +
                             "LEFT JOIN ADRESSE a ON c.IDADRESSE = a" +
                             ".IDADRESSE " +
                         "WHERE RAISONSOCIALECLIENT = ?");
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                Adresse adresse = new Adresse(
                        resultSet.getInt("IDADRESSE"),
                        resultSet.getString("NUMERORUE"),
                        resultSet.getString("NOMRUE"),
                        resultSet.getString("CODEPOSTAL"),
                        resultSet.getString("VILLE")
                );

                return new Client(
                        resultSet.getInt("IDCLIENT"),
                        resultSet.getString("RAISONSOCIALECLIENT"),
                        adresse,
                        resultSet.getString("TELEPHONECLIENT"),
                        resultSet.getString("EMAILCLIENT"),
                        resultSet.getString("COMMENTAIRESCLIENT"),
                        resultSet.getDouble("CHIFFREAFFAIRE"),
                        resultSet.getInt("NBREMPLOYES")
                );
            } else {
                return null;
            }
        } catch (CustomException ce) {
            LOGGER.severe("Une donnée de la BDD n'est pas conforme : "+ ce);
            throw new DaoException(Gravite.SEVERE,"Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter " +
                    "un administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche par nom de" +
                    " client dans la base de donnée : "+sqle);
            throw new DaoException(Gravite.SEVERE,"Un problème est survenu " +
                    "lors de la " +
                    "recherche par nom de" +
                    " client dans la base de donnée. Veuillez contacter " +
                    "un administrateur.\nFermeture de l'application.");
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche par nom de " +
                        "client : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est " +
                        "survenu lors de la" +
                        " " +
                        "recherche par nom de client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour créer un nouveau client dans la base de données.
     *
     * @param client Le client à créer
     * @throws Exception
     */
    @Override
    public void create(Client client) throws Exception {

        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = ConnectionDao.getConnection();
            connection.setAutoCommit(false);
            // Insérer l'adresse en premier
            statement = connection.prepareStatement(
                    "INSERT INTO ADRESSE (NUMERORUE, NOMRUE, CODEPOSTAL, VILLE) " +
                        "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            Adresse adresse = client.getAdresse();

            statement.setString(1, adresse.getNumeroRue());
            statement.setString(2, adresse.getNomRue());
            statement.setString(3, adresse.getCodePostal());
            statement.setString(4, adresse.getVille());
            statement.executeUpdate();

            // Recupérer ID de l'adresse généré
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int idAdresse = generatedKeys.getInt(1);

            // Insérer le client avec ID de l'adresse générer.
            statement = connection.prepareStatement(
                    "INSERT INTO CLIENT (IDADRESSE, RAISONSOCIALECLIENT, " +
                            "TELEPHONECLIENT, EMAILCLIENT, CHIFFREAFFAIRE, " +
                            "NBREMPLOYES, COMMENTAIRESCLIENT) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, idAdresse);
            statement.setString(2, client.getRaisonSociale());
            statement.setString(3, client.getTelephone());
            statement.setString(4, client.getEMail());
            statement.setDouble(5, client.getChiffreAffaires());
            statement.setInt(6, client.getNbrEmployes());
            statement.setString(7, client.getCommentaires());
            statement.executeUpdate();
            connection.commit();
        } catch (DataTruncation dt){
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est " +
                        "survenu lors de la " +
                        "creation d'un client. Veuillez contacter un administrateur." +
                        "\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(dt.getMessage());
            throw new DaoException(Gravite.INFO,"Il y a trop de caractères " +
                    "dans le champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est " +
                        "survenu lors de la" +
                        "creation d'un client. Veuillez contacter un administrateur." +
                        "\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new DaoException(Gravite.INFO,"Les informations renseigné " +
                    "dans " +
                    "le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre client.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la creation d'un client : "+sqle);
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                                "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est " +
                        "survenu lors de la creation d'un client. Veuillez " +
                        "contacter un administrateur.\nFermeture de l'application.");
            }

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la creation d'un " +
                        "client : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est " +
                        "survenu lors de la " +
                        "creation d'un client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }

    }

    /**
     * Méthode pour mettre à jour un client existant dans la base de données.
     *
     * @param client Le client à mettre à jour
     * @throws Exception
     */
    @Override
    public void update(Client client) throws Exception {

        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = ConnectionDao.getConnection();

            connection.setAutoCommit(false);
            Adresse adresse = client.getAdresse();
            // Maj adresse
            statement = connection.prepareStatement(
                    "UPDATE ADRESSE SET " +
                            "NUMERORUE = ?, " +
                            "NOMRUE = ?, " +
                            "CODEPOSTAL = ?, " +
                            "VILLE = ? " +
                        "WHERE IDADRESSE = ?");
            statement.setString(1, adresse.getNumeroRue());
            statement.setString(2, adresse.getNomRue());
            statement.setString(3, adresse.getCodePostal());
            statement.setString(4, adresse.getVille());
            statement.setInt(5, adresse.getIdentifiant());
            statement.executeUpdate();


            // Maj client
            statement = connection.prepareStatement(
                    "UPDATE CLIENT SET " +
                            "RAISONSOCIALECLIENT = ?, " +
                            "TELEPHONECLIENT = ?, " +
                            "EMAILCLIENT = ?, " +
                            "CHIFFREAFFAIRE = ?, " +
                            "NBREMPLOYES = ?, " +
                            "COMMENTAIRESCLIENT = ? " +
                        "WHERE IDCLIENT = ?");
            statement.setString(1, client.getRaisonSociale());
            statement.setString(2, client.getTelephone());
            statement.setString(3, client.getEMail());
            statement.setDouble(4, client.getChiffreAffaires());
            statement.setInt(5, client.getNbrEmployes());
            statement.setString(6, client.getCommentaires());
            statement.setInt(7, client.getIdentifiant());
            statement.executeUpdate();
            connection.commit();
        } catch (DataTruncation dt){
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(dt.getMessage());
            throw new DaoException(Gravite.INFO,"Il y a trop de caractères " +
                    "dans le champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un client. Veuillez contacter un administrateur." +
                        "\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new DaoException(Gravite.INFO,"Les informations renseigné " +
                    "dans le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre client.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la mise à jour d'un client : "+sqle);
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un client. Veuillez contacter un administrateur." +
                        "\nFermeture de l'application.");
            }

        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la mise à jour d'un " +
                        "client : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }

    }

    /**
     * Méthode pour supprimer un client de la base de données.
     *
     * @param client Le client à supprimer
     * @throws Exception
     */
    @Override
    public void delete(Client client) throws Exception {

        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = ConnectionDao.getConnection();
            connection.setAutoCommit(false);
            // Supprimmer client
            statement = connection.prepareStatement(
                    "DELETE FROM CLIENT WHERE IDCLIENT = ?"
            );
            statement.setInt(1, client.getIdentifiant());
            statement.executeUpdate();

            Adresse adresse = client.getAdresse();

            // Supprimmer adresse

            statement = connection.prepareStatement(
                    "DELETE FROM ADRESSE WHERE IDADRESSE = ?"
            );
            statement.setInt(1, adresse.getIdentifiant());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la suppression d'un " +
                    "client : " + sqle);
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "suppression d'un client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        } finally {
            try {
                if (statement != null) {
                        statement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la suppression d'un " +
                        "client : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "suppression d'un client. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }

    }
}
