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
        } catch (FileNotFoundException e) {
            throw new DaoException("Fichier de connection non trouvé");
        } catch (IOException e) {
            throw new DaoException("Soucis avec le fichier de connection");
        } catch (SQLException e) {
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
                if (connection != null) {

                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }



}
