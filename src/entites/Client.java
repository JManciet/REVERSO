package entites;

import exceptions.CustomException;

public class Client extends Societe {
    private double chiffreAffaires;
    private int nbrEmployes;

    public Client() {
    }

    public Client(Integer identifiant,
                  String raisonSociale,
                  Adresse adresse,
                  String telephone,
                  String eMail,
                  String commentaires,
                  double chiffreAffaires,
                  int nbrEmployes) throws CustomException {
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

    public void setChiffreAffaires(double chiffreAffaires) throws CustomException {

        if(chiffreAffaires <= 200){
            throw new CustomException("Le chiffre d’affaires doit être " +
                    "supérieur à 200");
        }
        this.chiffreAffaires = chiffreAffaires;
    }

    public int getNbrEmployes() { return nbrEmployes; }

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
