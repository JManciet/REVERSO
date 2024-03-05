package dao;

import entites.Adresse;
import entites.Client;
import exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utilitaires.Utilitaires.LOGGER;

public class ClientDao implements IDao<Client>{

    @Override
    public List<Client> findAll() throws DaoException, SQLException {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
            statement = connection.prepareStatement(
                     "SELECT c.*, a.* FROM CLIENT c " +
                             "INNER JOIN ADRESSE a ON c.IDADRESSE = a" +
                             ".IDADRESSE");
            ResultSet resultSet = statement.executeQuery();
            List<Client> clients = new ArrayList<>();
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
        }catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche des " +
                    "clients dans la base de donnée : "+ sqle);
            throw new DaoException("Un problème est survenu lors de la " +
                    "recherche des clients dans la base de donnée");
        }finally {
            if (statement != null) { statement.close(); }
        }
    }

    @Override
    public Client findByName(String nom) throws DaoException, SQLException {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
             statement = connection.prepareStatement(
                     "SELECT c.*, a.* FROM CLIENT c " +
                             "INNER JOIN ADRESSE a ON c.IDADRESSE = a.IDADRESSE " +
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
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche par nom de" +
                    " client dans la base de donnée : "+sqle);
            throw new DaoException("Un problème est survenu lors de la recherche par nom de" +
                    " client dans la base de donnée");
        }finally {
            if (statement != null) { statement.close(); }
        }
    }

    @Override
    public void create(Client client) throws DaoException, SQLException {

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
            statement.setString(4, client.geteMail());
            statement.setDouble(5, client.getChiffreAffaires());
            statement.setInt(6, client.getNbrEmployes());
            statement.setString(7, client.getCommentaires());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la creation d'un client");
            }
            throw new DaoException("La raison sociale du client existe déjà");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la creation d'un client : "+sqle);
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                                "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "creation d'un client");
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }

    }

    @Override
    public void update(Client client) throws DaoException, SQLException {

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
            statement.setString(3, client.geteMail());
            statement.setDouble(4, client.getChiffreAffaires());
            statement.setInt(5, client.getNbrEmployes());
            statement.setString(6, client.getCommentaires());
            statement.setInt(7, client.getIdentifiant());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un client");
            }
            throw new DaoException("La raison sociale du client existe déjà");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la mise à jour d'un client : "+sqle);
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un client");
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }

    }

    @Override
    public void delete(Client client) throws DaoException, SQLException {

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
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "suppression d'un client");
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }

    }
}
