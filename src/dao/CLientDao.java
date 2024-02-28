package dao;

import entites.Adresse;
import entites.Client;
import exceptions.DaoException;

import java.sql.*;
import java.util.List;

public class CLientDao implements IDao<Client>{
    @Override
    public List<Client> findAll() {
        return null;
    }

    @Override
    public Client findByName(String nom) {
        return null;
    }

    @Override
    public void create(Client client) throws DaoException {

        try (Connection connection = ConnectionDao.getConnection()) {
            // Insérer l'adresse en premier
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ADRESSE (NUMERORUE, NOMRUE, CODEPOSTAL, VILLE) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

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
            statement = connection.prepareStatement("INSERT INTO CLIENT (IDADRESSE, RAISONSOCIALECLIENT, TELEPHONECLIENT, EMAILCLIENT, CHIFFREAFFAIRE, NBREMPLOYES, COMMENTAIRESCLIENT) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, idAdresse);
            statement.setString(2, client.getRaisonSociale());
            statement.setString(3, client.getTelephone());
            statement.setString(4, client.geteMail());
            statement.setDouble(5, client.getChiffreAffaires());
            statement.setInt(6, client.getNbrEmployes());
            statement.setString(7, client.getCommentaires());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Client entity) {

    }

    @Override
    public void delete(Client entity) {

    }
}
