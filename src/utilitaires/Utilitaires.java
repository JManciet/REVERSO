package utilitaires;

import exceptions.CustomException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Utilitaires {

    public static final Logger LOGGER =
            Logger.getLogger(Utilitaires.class.getName());

    public static void creationLog() throws CustomException {

        FileHandler fh;
        try {
            fh = new FileHandler("logReverso.log", true);
        } catch (IOException e) {
            throw new CustomException("Problème lors de la création du " +
                    "fichier .log");
        }

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(fh);
        fh.setFormatter(new FormatterLog());

    }
}
