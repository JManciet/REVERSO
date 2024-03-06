package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import vues.Acceuil;
import vues.Affichage;
import vues.Formulaire;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControleurAcceuil {

    public Societe getSociete(TypeSociete choix, String nom) throws SQLException, DaoException, CustomException {
        Societe societe = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societe = new ClientDao().findByName(nom);
        }else{
            societe = new ProspectDao().findByName(nom);
        }
        return societe;
    }

    public ArrayList<String> listeNoms(TypeSociete choix) throws DaoException,
            SQLException, CustomException {

        ArrayList<String> result;
        if(choix.equals(TypeSociete.CLIENT)) {
            ArrayList<Client> clients = new ClientDao().findAll();
            ArrayList<String> nomClients = new ArrayList<>();
            for (Client client : clients) {
                nomClients.add(client.getRaisonSociale());
            }
            result = nomClients;
        }else{
            ArrayList<Prospect> prospects = new ProspectDao().findAll();
            ArrayList<String> nomProspects = new ArrayList<>();
            for (Prospect prospect : prospects) {
                nomProspects.add(prospect.getRaisonSociale());
            }
            result = nomProspects;
        }
        return result;
    };

//    public void suppression(TypeSociete choix,String nom) throws SQLException, DaoException {
//        if(choix.equals(TypeSociete.CLIENT)) {
//            ClientDao clientDao = new ClientDao();
//            Client client = clientDao.findByName(nom);
//            clientDao.delete(client);
//        }else{
//            ProspectDao prospectDao = new ProspectDao();
//            Prospect prospect = prospectDao.findByName(nom);
//            prospectDao.delete(prospect);
//        }
//    }

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
