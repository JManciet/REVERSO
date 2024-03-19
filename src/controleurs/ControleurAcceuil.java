package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import entites.Societe;
import utilitaires.TypeAction;
import utilitaires.TypeSociete;
import vues.Acceuil;
import vues.Affichage;
import vues.Formulaire;

import java.util.ArrayList;

/**
 * Gére les interactions de l'utilisateur sur la page d'accueil.
 * La classe permet de récupérer des informations sur les clients et les
 * prospects, d'ouvrir le formulaire de création/modification/suppression
 * et d'afficher la liste des clients ou des prospects.
 */
public class ControleurAcceuil {

    /**
     * Récupère une société en fonction du type et du nom fournis.
     *
     * @param choix Le type de société à rechercher (CLIENT ou PROSPECT)
     * @param nom Le nom de la société à rechercher
     * @return La société trouvée
     * @throws Exception
     */
    public Societe getSociete(TypeSociete choix, String nom) throws Exception {
        Societe societe = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societe = new ClientDao().findByName(nom);
        }else{
            societe = new ProspectDao().findByName(nom);
        }
        return societe;
    }


    /**
     * Récupère la liste des noms des sociétés en fonction du type de societé
     * fourni.
     *
     * @param choix Le type de société dont on veut récupérer la liste des noms (CLIENT ou PROSPECT)
     * @return Une liste contenant les noms des sociétés du type choisi
     * @throws Exception
     */
    public ArrayList<String> listeNoms(TypeSociete choix) throws Exception {

        ArrayList<String> result;
        if(choix.equals(TypeSociete.CLIENT)) {
            ArrayList<Societe> clients = new ClientDao().findAll();
            ArrayList<String> nomClients = new ArrayList<>();
            for (Societe client : clients) {
                nomClients.add(client.getRaisonSociale());
            }
            result = nomClients;
        }else{
            ArrayList<Societe> prospects = new ProspectDao().findAll();
            ArrayList<String> nomProspects = new ArrayList<>();
            for (Societe prospect : prospects) {
                nomProspects.add(prospect.getRaisonSociale());
            }
            result = nomProspects;
        }
        return result;
    };

    /**
     * Affiche la page d'accueil de l'application.
     */
    public static void pageAcceuil(){
        new Acceuil();
    }

    /**
     * Affiche le formulaire de création, modification ou de suppression d'une
     * société.
     *
     * @param choix Le type de société (CLIENT ou PROSPECT)
     * @param societe La société à modifier (peut être null en cas de création)
     * @param action Le type d'action à réaliser sur le formulaire (CREATION ou MODIFICATION)
     */
    public void pageFormulaire(TypeSociete choix, Societe societe,
                               TypeAction action){
        new Formulaire(choix, societe, action);
    }

    /**
     * Affiche la page d'affichage de la liste des sociétés en fonction du
     * type choisi.
     *
     * @param choix Le type de société dont on veut afficher la liste (CLIENT ou PROSPECT)
     */
    public void pageAffichage(TypeSociete choix) {
        new Affichage(choix);
    }

}
