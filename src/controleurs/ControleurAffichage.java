package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Societe;
import exceptions.DaoException;
import vues.Affichage;
import vues.Formulaire;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurAffichage {
    public ArrayList getListSociete(TypeSociete choix) throws SQLException,
            DaoException {
        ArrayList societes = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societes = new ClientDao().findAll();
        }else{
            societes = new ProspectDao().findAll();
        }
        return societes;
    }
}
