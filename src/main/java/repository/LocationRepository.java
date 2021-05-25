package repository;

import config.DatabaseConfig;
import models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Vector;

public class LocationRepository
{
    public void insert(Long id, String name, String address, Integer capacity) {
        String insertSql = "INSERT INTO Location(locationId, name, address, capacity) VALUES(?, ?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setInt(4, capacity);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public Optional<Location> getById(Long id) {
        String selectSql = "SELECT * FROM Location u WHERE u.locationId = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToLocation(resultSet);
        } catch (SQLException e)    {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<Location> mapToLocation(ResultSet resultSet) throws SQLException {
        if(resultSet.next())    {
            Long id = resultSet.getLong(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            Integer capacity = resultSet.getInt(4);
            return Optional.of(new Location(id, name, address, capacity));
        }
        return Optional.empty();
    }

    public void deleteLocation(Long id){
        GenericStatements.delete(id, "Location");
    }

    public Vector<Location> selectAll() {
        String selectSql = "SELECT * FROM Location";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        Vector<Location> locations = new Vector<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Location> location = mapToLocation(resultSet);
            while(location.isPresent())
            {
                locations.add(location.get());
                location = mapToLocation(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
}
