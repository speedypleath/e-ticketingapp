package repository;

import config.DatabaseConfig;
import exceptions.NoRoleException;
import models.Administrator;
import models.Client;
import models.Organiser;
import models.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository
{
    public void insert(String username, String name, String email, String password, String salt, String role) throws SQLIntegrityConstraintViolationException {
        String insertSql = "INSERT INTO User(username, name, email, password, salt, role) VALUES(?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, salt);
            preparedStatement.setString(6, role);
            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            throw e;
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public Optional<User> getByUsername(String username) throws NoRoleException{
        String selectSql = "SELECT * FROM User u WHERE u.username = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToUser(resultSet);
        } catch (SQLException e)    {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<User> mapToUser(ResultSet resultSet) throws SQLException, NoRoleException {
        if(resultSet.next())    {
            String username = resultSet.getString(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            String password = resultSet.getString(4);
            String salt = resultSet.getString(5);
            String role = resultSet.getString(6);
            if(role.equals("administrator"))
                return Optional.of(new Administrator(salt, username, password, email, name));
            else if(role.equals("organiser"))
                return Optional.of(new Organiser(salt, username, password, email, name));
            else if(role.equals("client"))
                return Optional.of(new Client(salt, username, password, email, name));
            else {
                deleteUser(username);
                throw new NoRoleException();
            }
        }
        return Optional.empty();
    }

    public void deleteUser(String username){
        String deleteSql = "DELETE FROM User u WHERE u.username = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e)    {
            e.printStackTrace();
        }
    }
}
