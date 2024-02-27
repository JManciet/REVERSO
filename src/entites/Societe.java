package entites;

public abstract class Societe {
    private int identifiant;
    private String raisonSociale;
    private Adresse adresse;
    private String telephone;
    private String eMail;
    private String commentaires;

    public Societe(String raisonSociale, Adresse adresse, String telephone, String eMail, String commentaires) {
        setRaisonSociale(raisonSociale);
        setAdresse(adresse);
        setTelephone(telephone);
        seteMail(eMail);
        setCommentaires(commentaires);
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }
}
