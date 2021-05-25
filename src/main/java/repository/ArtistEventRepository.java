package repository;

import models.Artist;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ArtistEventRepository
{
    public Set<Artist> getArtistsByEvent(Long eventId)
    {
        String selectSql = "select a.artistId, name, pseudonym from Artist a, Artist_Event e where a.artistId = e.artistId and eventId = ?;";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        Set<Artist> artists = new HashSet<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Artist> artist = GenericStatements.mapToArtist(resultSet);
            while(!artist.isEmpty())
            {
                artists.add(artist.get());
                artist = GenericStatements.mapToArtist(resultSet);
            }
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
        return artists;
    }
//    public void insert(Long id, String name, String pseudonym) {
//        String insertSql = "INSERT INTO Location(locationId, name, pseudonym) VALUES(?, ?, ?)";
//        Connection connection = DatabaseConfig.getDatabaseConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
//            preparedStatement.setLong(1, id);
//            preparedStatement.setString(2, name);
//            preparedStatement.setString(3, pseudonym);
//            preparedStatement.executeUpdate();
//        }
//        catch (SQLException e)    {
//            e.printStackTrace();
//        }
//    }
//
//    public Optional<Artist> getById(String username) {
//        String selectSql = "SELECT * FROM Artist u WHERE u.artistId = ?";
//        Connection connection = DatabaseConfig.getDatabaseConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
//            preparedStatement.setString(1, username);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return mapToArtist(resultSet);
//        } catch (SQLException e)    {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }
//
//    private Optional<Artist> mapToArtist(ResultSet resultSet) throws SQLException {
//        if(resultSet.next())    {
//            Long id = resultSet.getLong(1);
//            String name = resultSet.getString(2);
//            String pseudonym = resultSet.getString(3);
//            return Optional.of(new Artist(id, name, pseudonym));
//        }
//        return Optional.empty();
//    }
//
//    public void deleteArtist(Long id){
//        GenericStatements.delete(id, "Artist");
//    }
}
