package entites;

import exceptions.CustomException;

public abstract class Societe {
    private Integer identifiant;
    private String raisonSociale;
    private Adresse adresse;
    private String telephone;
    private String eMail;
    private String commentaires;

    public Societe(Integer identifiant, String raisonSociale, Adresse adresse,
                   String telephone, String eMail, String commentaires) throws CustomException {
        setIdentifiant(identifiant);
        setRaisonSociale(raisonSociale);
        setAdresse(adresse);
        setTelephone(telephone);
        seteMail(eMail);
        setCommentaires(commentaires);
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
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

    public void setTelephone(String telephone) throws CustomException {
        if(telephone.length()>=10) {
            this.telephone = telephone;
        }else{
           throw new CustomException("Le telephone doit avoir au moins 10 " +
                   "caractères") ;
        }
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

    @Override
    public String toString() {
        return "Societe{" +
                "identifiant=" + identifiant +
                ", raisonSociale='" + raisonSociale + '\'' +
                ", adresse=" + adresse +
                ", telephone='" + telephone + '\'' +
                ", eMail='" + eMail + '\'' +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }
}
