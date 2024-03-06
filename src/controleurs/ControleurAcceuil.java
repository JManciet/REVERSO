package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import entites.Societe;
import exceptions.DaoException;
import vues.Affichage;
import vues.Formulaire;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControleurAcceuil {

    public Societe getSociete(TypeSociete choix, String nom) throws SQLException, DaoException {
        Societe societe = null;
        if(choix.equals(TypeSociete.CLIENT)) {
            societe = new ClientDao().findByName(nom);
        }else{
            societe = new ProspectDao().findByName(nom);
        }
        return societe;
    }

    public List<String> listeNoms(TypeSociete choix) throws DaoException, SQLException {

        List result;
        if(choix.equals(TypeSociete.CLIENT)) {
            List<Client> clients = new ClientDao().findAll();
            List<String> nomClients = new ArrayList<>();
            for (Client client : clients) {
                nomClients.add(client.getRaisonSociale());
            }
            result = nomClients;
        }else{
            List<Prospect> prospects = new ProspectDao().findAll();
            List<String> nomProspects = new ArrayList<>();
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

    public void formulaire(TypeSociete choix,Societe societe,
                           TypeAction action){
        Formulaire formulaire = new Formulaire(choix, societe, action);
        formulaire.init();
    }

    public void affichage(TypeSociete choix) {
        Affichage affichage = new Affichage(choix);
        affichage.init();
    }

}
