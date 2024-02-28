package dao;

import entites.Adresse;
import entites.Prospect;
import exceptions.DaoException;

import java.sql.*;
import java.util.List;

public class ProspectDao implements IDao<Prospect>{
    @Override
    public List<Prospect> findAll() {
        return null;
    }

    @Override
    public Prospect findByName(String nom) throws DaoException {
        try (Connection connection = ConnectionDao.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT p.*, a.* FROM PROSPECT p " +
                             "INNER JOIN ADRESSE a ON p.IDADRESSE = a" +
                             ".IDADRESSE " +
                             "WHERE RAISONSOCIALEPROSPECT = ?")) {
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
                        resultSet.getString("INTERESSE")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Prospect prospect) throws DaoException {
        try (Connection connection = ConnectionDao.getConnection()) {
            // Insérer l'adresse en premier
            PreparedStatement statement = connection.prepareStatement(
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
            statement.setString(4, prospect.geteMail());
            statement.setDate(5, Date.valueOf(prospect.getDateProspection()));
            statement.setString(6, prospect.getInteresse());
            statement.setString(7, prospect.getCommentaires());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Prospect prospect) throws DaoException {
        try (Connection connection = ConnectionDao.getConnection()) {

            Adresse adresse = prospect.getAdresse();
            // Maj adresse
            PreparedStatement statement = connection.prepareStatement(
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
            statement.setString(3, prospect.geteMail());
            statement.setDate(4, Date.valueOf(prospect.getDateProspection()));
            statement.setString(5, prospect.getInteresse());
            statement.setString(6, prospect.getCommentaires());
            statement.setInt(7, prospect.getIdentifiant());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Prospect prospect) throws DaoException {
        try (Connection connection = ConnectionDao.getConnection()) {
            // Supprimmer prospect
            PreparedStatement statement = connection.prepareStatement(
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
