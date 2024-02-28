import dao.CLientDao;
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


        Adresse adresse = new Adresse("55","rue générale Leklerc","67400",
                "Nancy");
        Client client = new Client("REVERSO",adresse,"0606050408","contact" +
                "@reverso.fr","its good",5555555, 55);

        Prospect prospect = new Prospect("REVERSO",adresse,"0606050408",
                "contact" +
                "@reverso.fr","its good", LocalDate.now(), Interessement.NON);


        CLientDao clientDao = new CLientDao();
        ProspectDao prospectDao = new ProspectDao();
        try {
            prospectDao.create(prospect);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hello world!");
    }
}