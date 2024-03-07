package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import vues.Acceuil;
import vues.Affichage;
import vues.Formulaire;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurAffichage {


    public ArrayList getListSociete(TypeSociete choix) throws Exception {
        ArrayList societes = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societes = new ClientDao().findAll();
        }else{
            societes = new ProspectDao().findAll();
        }
        return societes;
    }

    public void retourAcceuil(){
        Acceuil acceuil = new Acceuil();
        acceuil.init();
    }
}
