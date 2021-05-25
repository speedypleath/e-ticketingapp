package repository;

import config.DatabaseConfig;
import models.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static repository.GenericStatements.mapToArtist;

public class ArtistRepository {
    public void insert(Long id, String name, String pseudonym) {
        String insertSql = "INSERT INTO Artist(artistId, name, pseudonym) VALUES(?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, pseudonym);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public Optional<Artist> getById(String username) {
        String selectSql = "SELECT * FROM Artist u WHERE u.artistId = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToArtist(resultSet);
        } catch (SQLException e)    {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void deleteArtist(Long id){
        GenericStatements.delete(id, "Artist");
    }

    public List<Artist> selectAll() {
        String selectSql = "SELECT * FROM Artist";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        List<Artist> artists = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Artist> artist = mapToArtist(resultSet);
            while(!artist.isEmpty())
            {
                artists.add(artist.get());
                artist = mapToArtist(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }
}
