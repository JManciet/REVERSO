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

public class ControleurAcceuil {

    public Societe getSociete(TypeSociete choix, String nom) throws Exception {
        Societe societe = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societe = new ClientDao().findByName(nom);
        }else{
            societe = new ProspectDao().findByName(nom);
        }
        return societe;
    }

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

    public static void pageAcceuil(){
        Acceuil acceuil = new Acceuil();
        acceuil.init();
    }

    public void pageFormulaire(TypeSociete choix, Societe societe,
                               TypeAction action){
        Formulaire formulaire = new Formulaire(choix, societe, action);
        formulaire.init();
    }

    public void pageAffichage(TypeSociete choix) {
        Affichage affichage = new Affichage(choix);
//        affichage.init();
    }

}
