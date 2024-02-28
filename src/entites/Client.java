package entites;

public class Client extends Societe {
    private double chiffreAffaires;
    private int nbrEmployes;

    public Client(Integer identifiant, String raisonSociale, Adresse adresse,
                  String telephone, String eMail, String commentaires, double chiffreAffaires, int nbrEmployes) {
        super(identifiant,raisonSociale, adresse, telephone, eMail,
                commentaires);
        setChiffreAffaires(chiffreAffaires);
        setNbrEmployes(nbrEmployes);
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }

    public void setChiffreAffaires(double chiffreAffaires) {
        this.chiffreAffaires = chiffreAffaires;
    }

    public int getNbrEmployes() {
        return nbrEmployes;
    }

    public void setNbrEmployes(int nbrEmployes) {
        this.nbrEmployes = nbrEmployes;
    }
}
