package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import exceptions.DaoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControleurAcceuil {

    public List liste(TypeSociete choix) throws DaoException, SQLException {

        List result;
        if(choix.equals(TypeSociete.CLIENT)) {
            List<Client> clients = new ClientDao().findAll();
            List<String> nomClients = new ArrayList<>();
            for (int i = 0; i < clients.size(); i++) {
                nomClients.add(clients.get(i).getRaisonSociale());
            }
            result = nomClients;
        }else{
            List<Prospect> prospects = new ProspectDao().findAll();
            List<String> nomProspects = new ArrayList<>();
            for (int i = 0; i < prospects.size(); i++) {
                nomProspects.add(prospects.get(i).getRaisonSociale());
            }
            result = nomProspects;
        }
        return result;
    };

}
