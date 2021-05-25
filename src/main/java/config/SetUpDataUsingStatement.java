package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetUpDataUsingStatement {
    public void createEventTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Event" +
                "(eventId bigint not null, " +
                "name varchar(40) not null, " +
                "description varchar(4000) not null, " +
                "date datetime not null, " +
                "organiserId varchar(40) not null, " +
                "locationId bigint null , " +
                "inviteLink varchar(60) null , " +
                "eventType varchar(10) not null, " +
                "PRIMARY KEY (eventId)," +
                "FOREIGN KEY (organiserId) REFERENCES User(username)," +
                "FOREIGN KEY (locationId) REFERENCES Location(locationId))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createLocationTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Location" +
                "(locationId bigint not null, " +
                "name varchar(40) not null, " +
                "address varchar(80) not null, " +
                "capacity int not null, " +
                "PRIMARY KEY (locationId))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createUserTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS User" +
                "(username varchar(40) not null, " +
                "name varchar(40) not null, " +
                "email varchar(80) not null, " +
                "password varchar(40) not null, " +
                "salt varchar(40) not null, " +
                "role varchar(15) not null, " +
                "PRIMARY KEY (username))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createArtistTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Artist" +
                "(artistId bigint not null, " +
                "name varchar(40) not null, " +
                "pseudonym varchar(80) not null, " +
                "PRIMARY KEY (artistId))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createArtistEventTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Artist_Event" +
                "(artistId bigint not null, " +
                "eventId bigint not null, " +
                "PRIMARY KEY (artistId, eventId), " +
                "FOREIGN KEY (artistId) REFERENCES Artist(artistId) ON DELETE CASCADE , " +
                "FOREIGN KEY (eventId) REFERENCES Event(eventId) ON DELETE CASCADE)";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createTicketTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Ticket" +
                "(ticketId bigint not null, " +
                "typeId bigint not null, " +
                "paymentId bigint null, " +
                "PRIMARY KEY (ticketId), " +
                "FOREIGN KEY (paymentId) REFERENCES Payment(paymentId), " +
                "FOREIGN KEY (typeId) REFERENCES TicketType(typeId))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createTicketTypeTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS TicketType" +
                "(typeId bigint not null,  " +
                "eventId bigint not null, " +
                "type varchar(40) not null, " +
                "price int not null, " +
                "PRIMARY KEY (typeId), " +
                "FOREIGN KEY (eventId) REFERENCES Event(eventId))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void createPaymentTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Payment" +
                "(paymentId bigint not null,  " +
                "date date not null, " +
                "clientId varchar(40) not null, " +
                "PRIMARY KEY (paymentId), " +
                "FOREIGN KEY (clientId) REFERENCES User(username))";

        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
