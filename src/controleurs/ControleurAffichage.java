package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Societe;
import utilitaires.TypeSociete;
import vues.Acceuil;

import java.util.ArrayList;

/**
 * Classe ControleurAffichage gérant les interactions de l'utilisateur sur la page d'affichage.
 * Elle permet de récupérer la liste des clients ou des prospects et de revenir à la page d'accueil.
 */
public class ControleurAffichage {


    /**
     * Récupère la liste des sociétés (Client ou Prospect) en fonction du
     * type choisi.
     *
     * @param choix Le type de société dont on veut afficher la liste (CLIENT ou PROSPECT)
     * @return Une liste contenant les sociétés du type choisi
     * @throws Exception
     */
    public ArrayList<Societe> getListSociete(TypeSociete choix) throws Exception {
        ArrayList<Societe> societes = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societes = new ClientDao().findAll();
        }else{
            societes = new ProspectDao().findAll();
        }
        return societes;
    }

    /**
     * Affiche la page d'accueil de l'application.
     */
    public void retourAcceuil(){
        new Acceuil();
    }
}
