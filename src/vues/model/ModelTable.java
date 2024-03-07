package vues.model;

import entites.*;
import utilitaires.Utilitaires;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ModelTable  extends AbstractTableModel {
    private ArrayList societes;

    private ArrayList<String> entetes = new ArrayList<>();

    public ModelTable(ArrayList societes) {
        super();

        this.societes = societes;

        entetes.add("Raison sociale");
        entetes.add("eMail");
        entetes.add("Téléphone");
        entetes.add("Numéro rue");
        entetes.add("Nom rue");
        entetes.add("Code Postal");
        entetes.add("Ville");

        for (Object obj : societes) {
            if (obj instanceof Client) {
                entetes.add("Chiffre d'affaire");
                entetes.add("Nombre d'employés");
                break;
            } else if (obj instanceof Prospect) {
                entetes.add("Date prospection");
                entetes.add("Interesse ?");
                break;
            }
        }
    }

    public int getRowCount() {
        return societes.size();
    }

    public int getColumnCount() {
        return entetes.size();
    }

    public String getColumnName(int columnIndex) {
        return entetes.get(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Societe societe = (Societe) societes.get(rowIndex);

        switch(columnIndex){
            case 0:
                return societe.getRaisonSociale();
            case 1:
                return societe.getEMail();
            case 2:
                return societe.getTelephone();
            case 3:
                return societe.getAdresse().getNumeroRue();
            case 4:
                return societe.getAdresse().getNomRue();
            case 5:
                return societe.getAdresse().getCodePostal();
            case 6:
                return societe.getAdresse().getVille();
            case 7:
                if(societe instanceof Client)
                    return new BigDecimal(((Client) societe).getChiffreAffaires()).toString();
                else if(societe instanceof Prospect)
                    return Utilitaires.formatDate(((Prospect) societe).getDateProspection());
            case 8:
                if(societe instanceof Client)
                    return ((Client) societe).getNbrEmployes();
                else if(societe instanceof Prospect) {
                    Interessement interessement =
                            ((Prospect) societe).getInteresse();
                    if (interessement != null) {
                        return ((Prospect) societe).getInteresse().getValue();
                    } else {
                        return "";
                    }
                }
            default:
                return null;
        }
    }
}
