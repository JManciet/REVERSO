package dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import entites.Adresse;
import entites.Societe;
import utilitaires.Interessement;
import entites.Prospect;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import java.sql.*;
import java.util.ArrayList;

import static utilitaires.Utilitaires.LOGGER;

public class ProspectDao implements IDao<Prospect>{

    @Override
    public ArrayList<Societe> findAll() throws Exception {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
             statement = connection.prepareStatement(
                     "SELECT p.*, a.* FROM PROSPECT p " +
                             "INNER JOIN ADRESSE a ON p.IDADRESSE = a" +
                             ".IDADRESSE");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Societe> prospects = new ArrayList<>();
            while (resultSet.next()) {

                Adresse adresse = new Adresse(
                        resultSet.getInt("IDADRESSE"),
                        resultSet.getString("NUMERORUE"),
                        resultSet.getString("NOMRUE"),
                        resultSet.getString("CODEPOSTAL"),
                        resultSet.getString("VILLE")
                );

                prospects.add(new Prospect(
                        resultSet.getInt("IDPROSPECT"),
                        resultSet.getString("RAISONSOCIALEPROSPECT"),
                        adresse,
                        resultSet.getString("TELEPHONEPROSPECT"),
                        resultSet.getString("EMAILPROSPECT"),
                        resultSet.getString("COMMENTAIRESPROSPECT"),
                        resultSet.getDate("DATEPROSPECTION").toLocalDate(),
                        resultSet.getString("INTERESSE").equals("null")? null:
                                Interessement.valueOf(resultSet.getString(
                                "INTERESSE"))
                ));
            }
            return prospects;
        } catch (CustomException ce) {
            LOGGER.severe("Une donnée de la BDD n'est pas conforme : "+ ce);
            throw new CustomException("Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter un administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche des " +
                    "prospects dans la base de donnée : "+ sqle);
            throw new DaoException("Un problème est survenu lors de la " +
                    "recherche des prospects dans la base de donnée. Veuillez contacter un administrateur.\nFermeture de l'application.");
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche des " +
                        "propects dans la base de donnée.: " + sqle);
                throw new DaoException("Un problème est survenu lors de la " +
                        "recherche des prospects dans la base de donnée. " +
                        "Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
        }
    }

    @Override
    public Prospect findByName(String nom) throws Exception {

        PreparedStatement statement = null;

        try {Connection connection = ConnectionDao.getConnection();
             statement = connection.prepareStatement(
                     "SELECT p.*, a.* FROM PROSPECT p " +
                             "INNER JOIN ADRESSE a ON p.IDADRESSE = a" +
                             ".IDADRESSE " +
                             "WHERE RAISONSOCIALEPROSPECT = ?");
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

                return new Prospect(
                        resultSet.getInt("IDPROSPECT"),
                        resultSet.getString("RAISONSOCIALEPROSPECT"),
                        adresse,
                        resultSet.getString("TELEPHONEPROSPECT"),
                        resultSet.getString("EMAILPROSPECT"),
                        resultSet.getString("COMMENTAIRESPROSPECT"),
                        resultSet.getDate("DATEPROSPECTION").toLocalDate(),
                        resultSet.getString("INTERESSE").equals("null")? null:
                                Interessement.valueOf(resultSet.getString(
                                        "INTERESSE"))
                );
            } else {
                return null;
            }
        } catch (CustomException ce) {
            LOGGER.severe("Une donnée de la BDD n'est pas conforme : "+ ce);
            throw new CustomException("Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter un administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche par nom de" +
                    " prospect dans la base de donnée : "+sqle);
            throw new DaoException("Un problème est survenu lors de la " +
                    "recherche par nom de prospect dans la base de donnée. Veuillez contacter un administrateur.\nFermeture de l'application.");
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche par nom de " +
                        "prospect : " + sqle);
                throw new DaoException("Un problème est survenu lors de la " +
                        "recherche par nom de prospect. Veuillez contacter un" +
                        " administrateur.\nFermeture de l'application.");
            }
        }
    }

    @Override
    public void create(Prospect prospect) throws Exception {

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

            Adresse adresse = prospect.getAdresse();

            statement.setString(1, adresse.getNumeroRue());
            statement.setString(2, adresse.getNomRue());
            statement.setString(3, adresse.getCodePostal());
            statement.setString(4, adresse.getVille());
            statement.executeUpdate();

            // Recupérer ID de l'adresse généré
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int idAdresse = generatedKeys.getInt(1);

            // Insérer le prospect avec ID de l'adresse générer.
            statement = connection.prepareStatement("INSERT INTO PROSPECT " +
                    "(IDADRESSE, RAISONSOCIALEPROSPECT, TELEPHONEPROSPECT, " +
                    "EMAILPROSPECT, DATEPROSPECTION, INTERESSE, " +
                    "COMMENTAIRESPROSPECT) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, idAdresse);
            statement.setString(2, prospect.getRaisonSociale());
            statement.setString(3, prospect.getTelephone());
            statement.setString(4, prospect.getEMail());
            statement.setDate(5, Date.valueOf(prospect.getDateProspection()));
            statement.setString(6, String.valueOf(prospect.getInteresse()));
            statement.setString(7, prospect.getCommentaires());
            statement.executeUpdate();
            connection.commit();
        } catch (MysqlDataTruncation mdt){
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(mdt.getMessage());
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la " +
                        "creation d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            throw new CustomException("Il y a trop de caractères dans le champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la creation d'un " +
                        "prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new CustomException("Les informations renseigné dans " +
                    "le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre prospect.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la creation d'un prospect : "+sqle);
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "creation d'un prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
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
                        "prospect : " + sqle);
                throw new DaoException("Un problème est survenu lors de la " +
                        "creation d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }

    @Override
    public void update(Prospect prospect) throws Exception {

        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = ConnectionDao.getConnection();

            connection.setAutoCommit(false);
            Adresse adresse = prospect.getAdresse();
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


            // Maj prospect
            statement = connection.prepareStatement(
                    "UPDATE PROSPECT SET " +
                            "RAISONSOCIALEPROSPECT = ?, " +
                            "TELEPHONEPROSPECT = ?, " +
                            "EMAILPROSPECT = ?, " +
                            "DATEPROSPECTION = ?, " +
                            "INTERESSE = ?, " +
                            "COMMENTAIRESPROSPECT = ? " +
                        "WHERE IDPROSPECT = ?");
            statement.setString(1, prospect.getRaisonSociale());
            statement.setString(2, prospect.getTelephone());
            statement.setString(3, prospect.getEMail());
            statement.setDate(4, Date.valueOf(prospect.getDateProspection()));
            statement.setString(5, String.valueOf(prospect.getInteresse()));
            statement.setString(6, prospect.getCommentaires());
            statement.setInt(7, prospect.getIdentifiant());
            statement.executeUpdate();
            connection.commit();
        } catch (MysqlDataTruncation mdt){
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(mdt.getMessage());
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            throw new CustomException("Il y a trop de caractères dans le " +
                    "champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new CustomException("Les informations renseigné dans " +
                    "le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre prospect.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la mise à jour d'un prospect : "+sqle);
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
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
                        "prospect : " + sqle);
                throw new DaoException("Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }

    @Override
    public void delete(Prospect prospect) throws Exception {

        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = ConnectionDao.getConnection();
            connection.setAutoCommit(false);
            // Supprimmer prospect
            statement = connection.prepareStatement(
                    "DELETE FROM PROSPECT WHERE IDPROSPECT = ?"
            );
            statement.setInt(1, prospect.getIdentifiant());
            statement.executeUpdate();

            Adresse adresse = prospect.getAdresse();

            // Supprimmer adresse

            statement = connection.prepareStatement(
                    "DELETE FROM ADRESSE WHERE IDADRESSE = ?"
            );
            statement.setInt(1, adresse.getIdentifiant());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la suppression d'un " +
                    "prospect : " + sqle);
            try {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException("Un problème est survenu lors de la " +
                        "suppression d'un prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
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
                        "prospect : " + sqle);
                throw new DaoException("Un problème est survenu lors de la " +
                        "suppression d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }
}
