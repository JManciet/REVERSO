package entites;

import exceptions.CustomException;
import utilitaires.Interessement;

import java.time.LocalDate;

/**
 * Classe Prospect représentant un prospect de l'entreprise.
 *
 * Elle hérite de la classe Société et ajoute des attributs spécifiques aux prospects.
 */
public class Prospect extends Societe{
    private LocalDate dateProspection;
    private Interessement interesse;

    /**
     * Constructeur de la classe Prospect.
     *
     * @param identifiant L'identifiant unique du prospect
     * @param raisonSociale La raison sociale du prospect (son nom)
     * @param adresse L'adresse postale du prospect
     * @param telephone Le numéro de téléphone du prospect
     * @param eMail L'adresse email du prospect
     * @param commentaires Des commentaires éventuels sur le prospect
     * @param dateProspection La date de la dernière prospection effectuée
     * @param interesse L'intérêt du prospect
     * @throws CustomException
     */
    public Prospect(
            Integer identifiant,
            String raisonSociale,
            Adresse adresse,
            String telephone,
            String eMail,
            String commentaires,
            LocalDate dateProspection,
            Interessement interesse
    ) throws Exception {
        super(
                identifiant,
                raisonSociale,
                adresse, telephone,
                eMail,
                commentaires
        );
        setDateProspection(dateProspection);
        setInteresse(interesse);
    }

    public LocalDate getDateProspection() {
        return dateProspection;
    }

    public void setDateProspection(LocalDate dateProspection) throws Exception {
        if (dateProspection == null) {
            throw new CustomException("La date de prospection n'est pas " +
                    "rentré");
        } else {
            this.dateProspection = dateProspection;
        }
    }

    public Interessement getInteresse() {
        return interesse;
    }

    public void setInteresse(Interessement interesse) throws Exception {
        if (interesse == null) {
            throw new CustomException("L'interessement n'est pas renseigné");
        } else {
            this.interesse = interesse;
        }
    }

    @Override
    public String toString() {
        return "Prospect{" +
                "dateProspection=" + dateProspection +
                ", interesse=" + interesse +
                '}'+" "+super.toString();
    }
}
