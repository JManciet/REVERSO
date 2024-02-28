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
    public Prospect findByName(String nom) {
        return null;
    }

    @Override
    public void create(Prospect prospect) throws DaoException {
        try (Connection connection = ConnectionDao.getConnection()) {
            // Insérer l'adresse en premier
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ADRESSE (NUMERORUE, NOMRUE, CODEPOSTAL, VILLE) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

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
                    "COMMENTAIRESPROSPECT) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, idAdresse);
            statement.setString(2, prospect.getRaisonSociale());
            statement.setString(3, prospect.getTelephone());
            statement.setString(4, prospect.geteMail());
            statement.setDate(5, Date.valueOf(prospect.getDateProspection()));
            statement.setString(6, prospect.getInteresse().getValue());
            statement.setString(7, prospect.getCommentaires());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Prospect entity) {

    }

    @Override
    public void delete(Prospect entity) {

    }
}
