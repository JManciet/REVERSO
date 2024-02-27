package utilitaires;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Utilitaires {

    public static final Logger LOGGER =
            Logger.getLogger(Utilitaires.class.getName());

    public static void creationLog(){

        FileHandler fh = null;
        try {
            fh = new FileHandler("logReverso.log", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(fh);
        fh.setFormatter(new FormatterLog());

    }
}
