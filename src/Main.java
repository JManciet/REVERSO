import dao.ConnectionDao;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import java.util.logging.Level;

import static utilitaires.Utilitaires.LOGGER;

public class Main {

    public static void main(String[] args) {

        try {
            Utilitaires.creationLog();
        } catch (CustomException e) {
            System.out.println("Erreur fichier .log. "+ e.getMessage() + " " + e);
        }

        try {
            ConnectionDao.getConnection();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        LOGGER.log(Level.INFO, "début pg");

        System.out.println("Hello world!");
    }
}