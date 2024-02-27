package entites;

import java.time.LocalDate;

public class Prospect extends Societe{
    private LocalDate dateProspection;
    private Interessement interesse;

    public Prospect(String raisonSociale, Adresse adresse, String telephone, String eMail, String commentaires, LocalDate dateProspection, Interessement interesse) {
        super(raisonSociale, adresse, telephone, eMail, commentaires);
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
}
