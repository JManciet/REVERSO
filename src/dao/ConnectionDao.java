package dao;

import exceptions.DaoException;
import utilitaires.Gravite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import static utilitaires.Utilitaires.LOGGER;

/**
 * Classe gérant la connexion à la base de données.
 *
 * Elle permet d'obtenir une connexion à la base de données afin de pouvoir effectuer des requêtes
 * et d'assurer la fermeture de la connexion à la fin du programme.
 */
public class ConnectionDao {

    private static Connection connection;

    /**
     * Constructeur privé qui initialise la connexion à la base de données à
     * l'aide des informations contenues dans le
     * fichier "database.properties".
     *
     * @throws Exception
     */
    private ConnectionDao() throws Exception {

        final Properties dataProperties = new Properties();
        File fichier = new File("database.properties");
        try {
            FileInputStream input = new FileInputStream(fichier);
            dataProperties.load(input);
            connection = DriverManager.getConnection(
                    dataProperties.getProperty("url"),
                    dataProperties.getProperty("login"),
                    dataProperties.getProperty("password")
            );
        } catch (FileNotFoundException fnfe) {
            LOGGER.severe("Fichier de connection non trouvé : "+fnfe);
            throw new DaoException(Gravite.SEVERE,"Fichier de connection non " +
                    "trouvé" + "\nFermeture de l'application");
        } catch (IOException ioe) {
            LOGGER.severe("Soucis avec le fichier de connection : "+ioe);
            throw new DaoException(Gravite.SEVERE, "Soucis avec le fichier de" +
                    " connection" + "\nFermeture de l'application");
        } catch (SQLException sqle) {
            LOGGER.severe("Connection avec la base de données non " +
                    "établie : "+sqle);
            throw new DaoException(Gravite.SEVERE, "Connection avec la base " +
                    "de données non établie" + "\nFermeture de l'application");
        }

    }

    /**
     * Méthode permettant d'obtenir la connexion à la base de données.
     *
     * Si la connexion n'est pas encore établie, elle est créée sinon
     * retourne la connexion déjà existante.
     *
     * @return La connexion à la base de données
     * @throws Exception en cas de problème lors de la connexion à la base de données
     */
    public static Connection getConnection() throws Exception {
        if (connection == null) {
            new ConnectionDao();
        }
        return connection;
    }

    /**
     * Permet de fermer la connexion à la base de données à la fin du programme.
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                LOGGER.log(Level.INFO, "fin pg");
                if (connection != null) {
                    try {
                        connection.close();
                        LOGGER.info("Database fermée");
                    } catch (SQLException e) {
                        LOGGER.severe(e.getMessage());
                    }
                }
            }
        });
    }



}
