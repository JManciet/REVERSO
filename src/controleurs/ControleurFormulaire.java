package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import entites.Societe;
import exceptions.DaoException;
import vues.Acceuil;
import vues.Formulaire;

import java.sql.SQLException;

public class ControleurFormulaire {

    public void createSociete(Societe societe) throws SQLException, DaoException {
        if(societe instanceof Client) {
            new ClientDao().create((Client)societe);
        }else{
            new ProspectDao().create((Prospect)societe);
        }
    }

    public void updateSociete(Societe societe) throws SQLException,
            DaoException {
        if(societe instanceof Client) {
            new ClientDao().update((Client)societe);
        }else{
            new ProspectDao().update((Prospect)societe);
        }
    }

    public void deleteSociete(Societe societe) throws SQLException,
            DaoException {
        if(societe instanceof Client) {
            new ClientDao().delete((Client)societe);
        }else{
            new ProspectDao().delete((Prospect)societe);
        }
    }

    public void retourAcceuil(){
        Acceuil acceuil = new Acceuil();
        acceuil.setSize(600,300);
        acceuil.setVisible(true);
    }

}
