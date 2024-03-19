package entites;

import exceptions.CustomException;

public class Adresse {
    private Integer identifiant;
    private String numeroRue;
    private String nomRue;
    private String codePostal;
    private String ville;


    public Adresse(Integer identifiant,
                   String numeroRue,
                   String nomRue,
                   String codePostal,
                   String ville) throws Exception {
        setIdentifiant(identifiant);
        setNumeroRue(numeroRue);
        setNomRue(nomRue);
        setCodePostal(codePostal);
        setVille(ville);
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getNumeroRue() {
        return numeroRue;
    }

    public void setNumeroRue(String numeroRue) throws Exception {
        if (numeroRue == null || numeroRue.isEmpty()) {
            throw new CustomException("Le numero de rue n'est pas rentré");
        } else {
            this.numeroRue = numeroRue;
        }
    }

    public String getNomRue() {
        return nomRue;
    }

    public void setNomRue(String nomRue) throws Exception {
        if (nomRue == null || nomRue.isEmpty()) {
            throw new CustomException("Le nom de rue n'est pas rentré");
        } else {
            this.nomRue = nomRue;
        }
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) throws Exception {
        if (codePostal == null || codePostal.isEmpty()) {
            throw new CustomException("Le code postal n'est pas rentré");
        } else {
            this.codePostal = codePostal;
        }
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) throws Exception {
        if (ville == null) {
            throw new CustomException("La ville n'est pas rentré");
        } else {
            this.ville = ville;
        }
    }


    @Override
    public String toString() {
        return "Adresse{" +
                "identifiant=" + identifiant +
                ", numeroRue='" + numeroRue + '\'' +
                ", nomRue='" + nomRue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }
}
