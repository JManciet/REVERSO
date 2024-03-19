package entites;

import exceptions.CustomException;
import utilitaires.Utilitaires;

/**
 * Classe abstraite représentant une société (Client ou Prospect).
 *
 * Cette classe définit les attributs et les méthodes communes aux clients et aux prospects.
 */
public abstract class Societe {
    private Integer identifiant;
    private String raisonSociale;
    private Adresse adresse;
    private String telephone;
    private String eMail;
    private String commentaires;

    /**
     * Constructeur par défaut.
     */
    public Societe() {
    }

    /**
     * Constructeur complet.
     *
     * @param identifiant Identifiant de la société
     * @param raisonSociale Raison sociale de la société
     * @param adresse Adresse de la société
     * @param telephone Numéro de téléphone de la société
     * @param eMail Adresse e-mail de la société
     * @param commentaires Commentaires sur la société
     * @throws Exception
     */
    public Societe(Integer identifiant, String raisonSociale, Adresse adresse,
                   String telephone, String eMail, String commentaires) throws Exception {
        setIdentifiant(identifiant);
        setRaisonSociale(raisonSociale);
        setAdresse(adresse);
        setTelephone(telephone);
        setEMail(eMail);
        setCommentaires(commentaires);
    }

    /**
     * Accesseur de l'identifiant.
     *
     * @return L'identifiant de la société
     */
    public Integer getIdentifiant() {
        return identifiant;
    }

    /**
     * Mutateur de l'identifiant.
     *
     * @param identifiant Le nouvel identifiant de la société
     */
    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Accesseur de la raison sociale.
     *
     * @return La raison sociale de la société
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Mutateur de la raison sociale.
     *
     * @param raisonSociale La nouvelle raison sociale de la société
     * @throws Exception
     */
    public void setRaisonSociale(String raisonSociale) throws Exception {
        if (raisonSociale == null || raisonSociale.isEmpty()) {
            throw new CustomException("La raison sociale n'est pas rentré");
        } else {
            this.raisonSociale = raisonSociale;
        }
    }

    /**
     * Accesseur de l'adresse.
     *
     * @return L'adresse de la société
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Mutateur de l'adresse.
     *
     * @param adresse La nouvelle adresse de la société
     */
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * Accesseur du numéro de téléphone.
     *
     * @return Le numéro de téléphone de la société
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Mutateur du numéro de téléphone qui vérifie si il a une taille minimum.
     *
     * @param telephone Le nouveau numéro de téléphone de la société
     * @throws CustomException
     */
    public void setTelephone(String telephone) throws CustomException {

        if (telephone == null || telephone.length() < 10) {
            throw new CustomException("Le telephone doit avoir au moins 10 " +
                    "caractères");
        }

        this.telephone = telephone;

    }

    /**
     * Accesseur de l'adresse e-mail.
     *
     * @return L'adresse e-mail de la société
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * Mutateur de l'adresse e-mail. Vérifie que l'adresse e-mail a le bon
     * format.
     *
     * @param eMail L'adresse e-mail de la société
     * @throws CustomException
     */
    public void setEMail(String eMail) throws CustomException {

        boolean valide = Utilitaires.PATTERN_MAIL.matcher(eMail).matches();

        if (!valide) {
            throw new CustomException("Le format du mail est invalid.\nIl " +
                    "devrait avoir comme format xx@zz.");
        }

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
