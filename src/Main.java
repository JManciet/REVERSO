import controleurs.ControleurAcceuil;
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
import vues.Acceuil;

import java.sql.SQLException;
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

        ControleurAcceuil.pageAcceuil();
    }
}