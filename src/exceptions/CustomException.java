package exceptions;

/**
 * Classe représentant une exception personnalisée générique.
 *
 * Cette exception peut être utilisée pour lever une erreur dans n'importe quelle partie de l'application.
 * Elle est paramétrée avec un message d'erreur qui sera transmis à l'utilisateur.
 */
public class CustomException extends Exception{

    /**
     * Constructeur de l'exception CustomException.
     *
     * @param message Le message d'erreur à afficher à l'utilisateur
     */
    public CustomException(String message) {
        super(message);
    }
}
