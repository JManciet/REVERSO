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

    public Societe getSociete(TypeSociete choix, String nom) throws SQLException, DaoException {
        Societe societe = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societe = new ClientDao().findByName(nom);
        }else{
            societe = new ProspectDao().findByName(nom);
        }
        return societe;
    }

    public void createSociete(Societe societe) throws SQLException, DaoException {
        if(societe instanceof Client) {
            new ClientDao().create((Client)societe);
        }else{
            new ProspectDao().create((Prospect)societe);
        }
        retourAcceuil();
    }

    public void updateSociete(Societe societe) throws SQLException,
            DaoException {
        if(societe instanceof Client) {
            new ClientDao().update((Client)societe);
        }else{
            new ProspectDao().update((Prospect)societe);
        }
        retourAcceuil();
    }

    public void deleteSociete(Societe societe) throws SQLException,
            DaoException {
        if(societe instanceof Client) {
            new ClientDao().delete((Client)societe);
        }else{
            new ProspectDao().delete((Prospect)societe);
        }
        retourAcceuil();
    }

    public void retourAcceuil(){
        Acceuil acceuil = new Acceuil();
        acceuil.setSize(600,300);
        acceuil.setVisible(true);
    }

}
