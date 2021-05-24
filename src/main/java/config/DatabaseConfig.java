package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/proiect_PAO?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Bucharest";
    private static final String USER = "root";
    private static final String PASSWORD = "Parola123!";

    private static Connection databaseConnection;

    private DatabaseConfig() {
    }

    public static Connection getDatabaseConnection()    {
        try {
            if(databaseConnection == null || databaseConnection.isClosed()) {
                databaseConnection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return databaseConnection;
    }

    public static void closeDatabaseConnection()    {
        try {
            if(databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args){
        Connection connection = DatabaseConfig.getDatabaseConnection();
        SetUpDataUsingStatement statement = new SetUpDataUsingStatement();
        statement.createUserTable();
        statement.createLocationTable();
        statement.createEventTable();
        statement.createArtistTable();
        statement.createArtistEventTable();
        DatabaseConfig.closeDatabaseConnection();
    }
}