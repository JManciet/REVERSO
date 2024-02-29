package controleurs;

import dao.ClientDao;
import entites.Societe;
import exceptions.DaoException;

import java.sql.SQLException;

public class ControleurFormulaire {

    public Societe societe(TypeSociete choix, String nom) throws SQLException, DaoException {
        return new ClientDao().findByName(nom);
    }

}
