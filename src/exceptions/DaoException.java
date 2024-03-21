package exceptions;

import utilitaires.Gravite;

/**
 * Classe représentant une exception générique survenant lors d'un accès aux données.
 *
 * Cette exception est utilisée pour regrouper les erreurs SQL et les autres
 * types d'erreurs pouvant survenir lors de l'interaction avec la base de
 * données.
 */
public class DaoException extends Exception{

    private Gravite gravite;

//    /**
//     * Constructeur de l'exception DaoException.
//     *
//     * @param message Le message d'erreur à afficher à l'utilisateur
//     */
//    public DaoException(String message) {
//        super(message);
//    }

    public DaoException(Gravite gravite, String message) {
        super(message);
        this.gravite = gravite;
    }

    public Gravite getGravite() {
        return gravite;
    }
}
