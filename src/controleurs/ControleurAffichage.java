package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Societe;
import utilitaires.TypeSociete;
import vues.Acceuil;

import java.util.ArrayList;

public class ControleurAffichage {


    public ArrayList<Societe> getListSociete(TypeSociete choix) throws Exception {
        ArrayList<Societe> societes = null;
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
