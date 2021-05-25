package repository;

import config.DatabaseConfig;
import models.SoldTicket;
import models.Ticket;
import models.TicketType;

import java.sql.*;
import java.util.Optional;

public class TicketRepository
{
    public void insert(Ticket ticket) {
        String insertSql = "INSERT INTO Ticket(ticketId, typeId, clientId) VALUES(?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, ticket.getId());
            preparedStatement.setLong(2, ticket.getType().getId());
            if(ticket instanceof SoldTicket)
                preparedStatement.setString(3, ((SoldTicket) ticket).getClient().getUsername());
            else
                preparedStatement.setNull(3, Types.VARCHAR);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public Optional<TicketType> selectType(Long eventId, String type, Integer price)
    {
        String selectSql = "SELECT * FROM TicketType WHERE eventId = ? and type = ? and price = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, eventId);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, price);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToTicketType(resultSet);
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insertType(TicketType ticketType)
    {
        if(selectType(ticketType.getEvent().getId(), ticketType.getType(), ticketType.getPrice()).isPresent())
            return;
        String selectSql = "INSERT INTO TicketType(typeId, eventId, type, price) VALUES(?, ?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, ticketType.getId());
            preparedStatement.setLong(2, ticketType.getEvent().getId());
            preparedStatement.setString(3, ticketType.getType());
            preparedStatement.setInt(4, ticketType.getPrice());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }
    private Optional<TicketType> mapToTicketType(ResultSet resultSet) throws SQLException {
        if(resultSet.next())    {
            Long id = resultSet.getLong(1);
            Long eventId = resultSet.getLong(2);
            String type = resultSet.getString(3);
            Integer price = resultSet.getInt(4);
            EventRepository eventRepository = new EventRepository();
            return Optional.of(new TicketType(id, eventRepository.getById(eventId).get(), type, price));
        }
        return Optional.empty();
    }
}
