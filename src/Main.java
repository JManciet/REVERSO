import controleurs.ControleurAcceuil;
import exceptions.CustomException;
import utilitaires.Utilitaires;

import javax.swing.*;
import java.util.logging.Level;

import static utilitaires.Utilitaires.LOGGER;

public class Main {

    public static void main(String[] args) {

        try {
            Utilitaires.creationLog();
        } catch (CustomException e) {
            JOptionPane.showMessageDialog(null, "Un problème est survenue. " +
                    e.getMessage() + "\nFermeture de l'application.");
            System.exit(1);
        }

        LOGGER.log(Level.INFO, "début pg");

        ControleurAcceuil.pageAcceuil();
    }
}