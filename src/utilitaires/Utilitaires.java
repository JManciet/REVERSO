package utilitaires;

import entites.Societe;
import exceptions.CustomException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utilitaires {

    /**
     * Logger utilisé pour enregistrer les événements de l'application.
     */
    public static final Logger LOGGER =
            Logger.getLogger(Utilitaires.class.getName());

    /**
     * Expression régulière pour la validation des adresses e-mail.
     */
    public static final Pattern PATTERN_MAIL =
            Pattern.compile("^(.+)@(.+)$");

    /**
     * Crée un fichier de log pour enregistrer les événements de l'application.
     *
     * @throws CustomException Si la création du fichier de log échoue.
     */
    public static void creationLog() throws CustomException {

        FileHandler fh;
        try {
            fh = new FileHandler("logReverso.log", true);
        } catch (IOException e) {
//            LOGGER.log(Level.SEVERE, "Problème lors de la création du fichier .log", e);
            throw new CustomException("Problème lors de la création du " +
                    "fichier qui enregistre les événements de l'application.");
        }

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(fh);
        fh.setFormatter(new FormatterLog()); // On suppose la présence d'une classe FormatterLog

    }

    /**
     * Formate une date LocalDate au format européen (jj/MM/yyyy).
     *
     * @param localDate La date à formater
     * @return La date formatée au format européen
     */
    public static String formatDate(LocalDate localDate) {

        String europeanDatePattern = "dd/MM/yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        return europeanDateFormatter.format(localDate);
    }

    /**
     * Formate un montant pour l'affichage avec deux décimales et la virgule.
     *
     * @param valeur Le montant à formater
     * @return Le montant formaté avec deux décimales avec affichage en virgule
     */
    public static String formatMoneyForDisplay(double valeur) {

        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(valeur);
    }

    /**
     * Formate une chaîne de caractères représentant un montant pour
     * l'enregistrement en base de données (arrondi à deux décimales).
     *
     * @param valeur La chaîne représentant le montant
     * @return Le montant converti en double et arrondi à deux décimales
     */
    public static double formatMoneyForSave(String valeur) {

        return Math.round(Double.parseDouble(valeur) * 100) / 100.0;
    }

    /**
     * Trie une liste de Sociétés par ordre croissant de raison sociale.
     *
     * @param societes La liste des sociétés à trier
     * @return La liste des sociétés triée par raison sociale
     */
    public static ArrayList<Societe> sortSocieteByRaisonSociale(ArrayList<Societe> societes) {
        return  societes.stream()
                .sorted(Comparator.comparing(Societe::getRaisonSociale))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Détermine le nom du champ qui a causé une exception de type
     * SQLException, à partir du message d'erreur.
     *
     * @param errorMessage Le message d'erreur contenant la référence au champ
     * @return Le nom du champ en français, ou "non déterminé" si le champ n'a pas pu être identifié
     */
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

    /**
     * Extrait la valeur à l'origine d'une excection de type
     * NumberFormatException
     *
     * @param nfe L'exception NumberFormatException contenant la valeur à extraire
     * @return La valeur à l'origine de l'exception, ou "non déterminé" si la valeur n'a pas pu être extraite
     */
    public static String valueAsGetException(String nfe) {

        //regex permettant d'extraire du message d'erreur la valeur entre
        // guillemet et qui corespond a la valeur saisi par l'utilisateur
        // dans le champ du formulaire
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
