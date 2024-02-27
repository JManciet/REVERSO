import dao.ConnectionDao;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import java.util.logging.Level;

import static utilitaires.Utilitaires.LOGGER;

public class Main {

    public static void main(String[] args) {

        Utilitaires.creationLog();

        try {
            ConnectionDao.getConnection();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        LOGGER.log(Level.INFO, "début pg");

        System.out.println("Hello world!");
    }
}