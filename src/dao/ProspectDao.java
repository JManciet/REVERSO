package dao;

import entites.Adresse;
import entites.Societe;
import utilitaires.Gravite;
import utilitaires.Interessement;
import entites.Prospect;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import java.sql.*;
import java.util.ArrayList;

import static utilitaires.Utilitaires.LOGGER;

/**
 * Classe implémentant l'interface `IDao` pour les prospects.
 * Permet de gérer les opérations CRUD (création, lecture, mise à jour,
 * suppression) sur les prospects dans la base de données.
 */
public class ProspectDao implements IDao<Prospect>{

    /**
     * Méthode pour récupérer tous les prospects de la base de données.
     *
     * @return Une liste de prospects
     * @throws Exception
     */
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
            throw new DaoException(Gravite.SEVERE,"Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter" +
                    " un administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche des " +
                    "prospects dans la base de donnée : "+ sqle);
            throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                    "recherche des prospects dans la base de donnée. Veuillez " +
                    "contacter un administrateur.\nFermeture de l'application.");
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche des " +
                        "propects dans la base de donnée.: " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "recherche des prospects dans la base de donnée. " +
                        "Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour rechercher un prospect par son nom.
     *
     * @param nom Le nom du prospect à rechercher
     * @return Le prospect trouvé ou null si aucun prospect n'est trouvé
     * @throws Exception
     */
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
            throw new DaoException(Gravite.SEVERE,"Problème avec les données " +
                    "enregistrées dans la base de donnée. Veuillez contacter un" +
                    " administrateur.\nFermeture de l'application.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la recherche par nom de" +
                    " prospect dans la base de donnée : "+sqle);
            throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                    "recherche par nom de prospect dans la base de donnée. Veuillez" +
                    " contacter un administrateur.\nFermeture de l'application.");
        }finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqle) {
                LOGGER.severe("Problème lors de la recherche par nom de " +
                        "prospect : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "recherche par nom de prospect. Veuillez contacter un" +
                        " administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour créer un nouveau prospect dans la base de données.
     *
     * @param prospect Le prospect à créer
     * @throws Exception
     */
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
        } catch (DataTruncation dt){
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(dt.getMessage());
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "creation d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            throw new DaoException(Gravite.INFO,"Il y a trop de caractères dans le champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la creation d'un " +
                        "prospect. Veuillez contacter un administrateur.\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new DaoException(Gravite.INFO,"Les informations renseigné dans " +
                    "le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre prospect.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la creation d'un prospect : "+sqle);
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
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
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "creation d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour mettre à jour un prospect existant dans la base de données.
     *
     * @param prospect Le prospect à mettre à jour
     * @throws Exception
     */
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
        } catch (DataTruncation dt){
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(dt.getMessage());
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            throw new DaoException(Gravite.INFO,"Il y a trop de caractères dans le " +
                    "champ "+champEnCause);
        } catch (SQLIntegrityConstraintViolationException sqlicve) {
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe(excep.toString());
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
            String champEnCause =
                    Utilitaires.fieldAsGenerateException(sqlicve.getMessage());
            throw new DaoException(Gravite.INFO,"Les informations renseigné dans " +
                    "le champ "+ champEnCause +" ne peut être identique à un" +
                    " autre prospect.");
        } catch (SQLException sqle) {
            LOGGER.severe("Problème lors de la mise à jour d'un prospect : "+sqle);
            try {
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
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
                LOGGER.severe("Problème lors de la mise à jour d'un " +
                        "prospect : " + sqle);
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "mise à jour d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }

    /**
     * Méthode pour supprimer un prospect de la base de données.
     *
     * @param prospect Le prospect à supprimer
     * @throws Exception
     */
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
                connection.rollback();
            } catch (SQLException excep) {
                LOGGER.severe("Problème lors de l'annulation de la " +
                        "transaction : "+excep);
            } finally {
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "suppression d'un prospect. Veuillez contacter " +
                        "un administrateur.\nFermeture de l'application.");
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
                throw new DaoException(Gravite.SEVERE,"Un problème est survenu lors de la " +
                        "suppression d'un prospect. Veuillez contacter un " +
                        "administrateur.\nFermeture de l'application.");
            }
        }
    }
}
