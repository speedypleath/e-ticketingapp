package repository;

import models.Artist;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GenericStatements
{
    public static void delete(Long id, String table){
        String deleteSql = "DELETE FROM "+ table + " WHERE " + table + "Id = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public static Optional<Artist> mapToArtist(ResultSet resultSet) throws SQLException {
        if(resultSet.next())    {
            Long id = resultSet.getLong(1);
            String name = resultSet.getString(2);
            String pseudonym = resultSet.getString(3);
            return Optional.of(new Artist(id, name, pseudonym));
        }
        return Optional.empty();
    }
}
