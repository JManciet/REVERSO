package utilitaires;

import exceptions.CustomException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilitaires {

    public static final Logger LOGGER =
            Logger.getLogger(Utilitaires.class.getName());

    public static final Pattern PATTERN_MAIL =
            Pattern.compile("^(.+)@(.+)$");

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

    public static String formatDate(LocalDate localDate){

        String europeanDatePattern = "dd/MM/yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        return europeanDateFormatter.format(localDate);
    }

    public static String aroundTwoDecimalAndFormat(double valeur){

        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(valeur);
    }

    public static double aroundTwoDecimalAndFormat(String valeur){

        valeur = valeur.replace(",",".");
        return Math.round(Double.valueOf(valeur) * 100) / 100.0;
    }

    public static String fieldAsGenerateException(String errorMessage) {
        if(errorMessage.contains("RAISONSOCIALE")) return "RAISON SOCIALE";
        if(errorMessage.contains("TELEPHONE")) return "TELEPHONE";
        if(errorMessage.contains("EMAIL")) return "EMAIL";
        if(errorMessage.contains("CHIFFREAFFAIRE")) return "CHIFFRE D'AFFAIRE";
        if(errorMessage.contains("NBREMPLOYES")) return "NOMBRE D'EMPLOYES";
        if(errorMessage.contains("COMMENTAIRES")) return "COMMENTAIRES";
        if(errorMessage.contains("NUMERORUE")) return "NUMERO";
        if(errorMessage.contains("NOMRUE")) return "RUE";
        if(errorMessage.contains("CODEPOSTAL")) return "CODE POSTAL";
        if(errorMessage.contains("VILLE")) return "VILLE";
        return "non determiné";
    }

    public static String valueAsGetException(String nfe) {

        Pattern pattern = Pattern.compile("(?<=\").*(?=\")");
        Matcher matcher = pattern.matcher(nfe);
        System.out.println(nfe);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "non determiné";
        }
    }
}
