package entites;

import exceptions.CustomException;

/**
 * Classe représentant un client.
 *
 * Elle hérite de la classe Société et ajoute des attributs spécifiques aux
 * clients.
 */
public class Client extends Societe {
    private double chiffreAffaires;
    private int nbrEmployes;

    /**
     * Constructeur par défaut
     */
    public Client() {
    }

    /**
     * Constructeur complet
     *
     * @param identifiant       Identifiant unique du client
     * @param raisonSociale    Raison sociale du client
     * @param adresse          Adresse du client
     * @param telephone        Numéro de téléphone du client
     * @param eMail            Adresse email du client
     * @param commentaires     Commentaires sur le client
     * @param chiffreAffaires  Chiffre d'affaires du client
     * @param nbrEmployes      Nombre d'employés du client
     * @throws CustomException Si le chiffre d'affaires est inférieur ou égal à 200
     *                         ou si le nombre d'employés est inférieur à 1
     */
    public Client(Integer identifiant,
                  String raisonSociale,
                  Adresse adresse,
                  String telephone,
                  String eMail,
                  String commentaires,
                  double chiffreAffaires,
                  int nbrEmployes) throws Exception {
        super(identifiant,
                raisonSociale,
                adresse,
                telephone,
                eMail,
                commentaires);
        setChiffreAffaires(chiffreAffaires);
        setNbrEmployes(nbrEmployes);
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }

    /**
     * Accesseur en écriture sur le chiffre d'affaires
     *
     * @param chiffreAffaires Le nouveau chiffre d'affaires du client
     * @throws CustomException Si le chiffre d'affaires est inférieur ou égal à 200
     */
    public void setChiffreAffaires(double chiffreAffaires) throws CustomException {

        if(chiffreAffaires <= 200){
            throw new CustomException("Le chiffre d’affaires doit être " +
                    "supérieur à 200");
        }
        this.chiffreAffaires = chiffreAffaires;
    }

    public int getNbrEmployes() { return nbrEmployes; }

    /**
     * Accesseur en écriture sur le nombre d'employés
     *
     * @param nbrEmployes Le nouveau nombre d'employés du client
     * @throws CustomException Si le nombre d'employés est inférieur à 1
     */
    public void setNbrEmployes(int nbrEmployes) throws CustomException {

        if(nbrEmployes < 1){
            throw new CustomException("Le nombre d'employé doit être " +
                    "supérieur à 0");
        }

        this.nbrEmployes = nbrEmployes;
    }

    @Override
    public String toString() {
        return "Client{" +
                "chiffreAffaires=" + chiffreAffaires +
                ", nbrEmployes=" + nbrEmployes +
                '}'+" "+super.toString();
    }
}
