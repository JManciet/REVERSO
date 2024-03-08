package entites;

import exceptions.CustomException;
import utilitaires.Interessement;

import java.time.LocalDate;

public class Prospect extends Societe{
    private LocalDate dateProspection;
    private Interessement interesse;

    public Prospect(
            Integer identifiant,
            String raisonSociale,
            Adresse adresse,
            String telephone,
            String eMail,
            String commentaires,
            LocalDate dateProspection,
            Interessement interesse
    ) throws CustomException {
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

    public void setDateProspection(LocalDate dateProspection) {
        this.dateProspection = dateProspection;
    }

    public Interessement getInteresse() {
        return interesse;
    }

    public void setInteresse(Interessement interesse) {
        this.interesse = interesse;
    }

    @Override
    public String toString() {
        return "Prospect{" +
                "dateProspection=" + dateProspection +
                ", interesse=" + interesse +
                '}'+" "+super.toString();
    }
}
