package entites;

import java.time.LocalDate;

public class Prospect extends Societe{
    private LocalDate dateProspection;
    private String interesse;

    public Prospect(Integer identifiant, String raisonSociale, Adresse adresse,
                    String telephone,
                    String eMail, String commentaires, LocalDate dateProspection, String interesse) {
        super(identifiant, raisonSociale, adresse, telephone, eMail,
                commentaires);
        setDateProspection(dateProspection);
        setInteresse(interesse);
    }

    public LocalDate getDateProspection() {
        return dateProspection;
    }

    public void setDateProspection(LocalDate dateProspection) {
        this.dateProspection = dateProspection;
    }

    public String getInteresse() {
        return interesse;
    }

    public void setInteresse(String interesse) {
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
