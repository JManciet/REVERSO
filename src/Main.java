import controleurs.ControleurAcceuil;
import exceptions.CustomException;
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

        LOGGER.log(Level.INFO, "début pg");

        ControleurAcceuil.pageAcceuil();
    }
}