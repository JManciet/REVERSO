package controleurs;

import dao.ClientDao;
import dao.ProspectDao;
import entites.Client;
import entites.Prospect;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import vues.Acceuil;
import vues.Formulaire;

import java.sql.SQLException;

/**
 * Classe gérant les interactions de l'utilisateur sur le formulaire.
 * Elle permet de créer, modifier et supprimer des Sociétés (Client ou Prospect)
 * et de retourner à la page d'accueil.
 */
public class ControleurFormulaire {

    /**
     * Crée une nouvelle société (Client ou Prospect) dans la base de données.
     *
     * @param societe La société à créer
     * @throws Exception
     */
    public void createSociete(Societe societe) throws Exception {
        if (societe instanceof Client) {
            new ClientDao().create((Client) societe);
        } else {
            new ProspectDao().create((Prospect) societe);
        }
    }

    /**
     * Met à jour une société (Client ou Prospect).
     *
     * @param societe La société à mettre à jour
     * @throws Exception
     */
    public void updateSociete(Societe societe) throws Exception {
        if (societe instanceof Client) {
            new ClientDao().update((Client) societe);
        } else {
            new ProspectDao().update((Prospect) societe);
        }
    }

    /**
     * Supprime une société (Client ou Prospect) de la base de données.
     *
     * @param societe La société à supprimer
     * @throws Exception
     */
    public void deleteSociete(Societe societe) throws Exception {
        if (societe instanceof Client) {
            new ClientDao().delete((Client) societe);
        } else {
            new ProspectDao().delete((Prospect) societe);
        }
    }

    /**
     * Retourne à la page d'accueil de l'application.
     */
    public void retourAcceuil() {
        new Acceuil();
    }
}
