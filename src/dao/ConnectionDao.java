package dao;

import exceptions.DaoException;

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

public class ConnectionDao {

    private static Connection connection;

    private ConnectionDao() throws DaoException {

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
            fnfe.printStackTrace();
            throw new DaoException("Fichier de connection non trouvé");
        } catch (IOException ioe) {
            LOGGER.severe("Soucis avec le fichier de connection : "+ioe);
            throw new DaoException("Soucis avec le fichier de connection");
        } catch (SQLException sqle) {
            LOGGER.severe("Connection avec la base de données non " +
                    "établie : "+sqle);
            sqle.printStackTrace();
            throw new DaoException("Connection avec la base de données non " +
                    "établie");
        }


    }

    public static Connection getConnection() throws DaoException {
        if (connection == null) {
            new ConnectionDao();
        }
        return connection;
    }

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
