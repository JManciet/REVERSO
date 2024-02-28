import dao.ClientDao;
import dao.ConnectionDao;
import dao.ProspectDao;
import entites.Adresse;
import entites.Client;
import entites.Interessement;
import entites.Prospect;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;

import static utilitaires.Utilitaires.LOGGER;

public class Main {

    public static void main(String[] args) {


        try {
            Utilitaires.creationLog();
        } catch (CustomException e) {
            System.out.println("Erreur fichier .log. "+ e.getMessage() + " " + e);
        }

        LOGGER.log(Level.INFO, "début pg");

        try {
            ConnectionDao.getConnection();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }


        Adresse adresse = new Adresse(1,"55","rue générale Papin",
                "67400",
                "Nancy");
        Client client = new Client(1,"MAn++",adresse,"0606050408",
                "contact" +
                "@reverso.fr","its good",5555555, 55);

        Prospect prospect = new Prospect(1,"REVERSOCHOUETTE",adresse,
                "0606050408",
                "contact" +
                "@reverso.fr","its good", LocalDate.now(), Interessement.NON.getValue());


        ClientDao clientDao = new ClientDao();
        ProspectDao prospectDao = new ProspectDao();

        try {

//            clientDao.create(client);

            List<Prospect> clients = prospectDao.findAll();

            for (int i = 0; i < clients.size(); i++) {
                System.out.println(clients.get(i));
            }

        } catch (DaoException e) {
            System.out.println("Nom d'entreprise déjà enregistré");
        }



        System.out.println("Hello world!");
    }
}