package repository;

import config.DatabaseConfig;
import models.Payment;
import models.SoldTicket;
import models.Ticket;
import models.TicketType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository
{
    public void insert(Ticket ticket) {
        String insertSql = "INSERT INTO Ticket(ticketId, typeId, paymentId) VALUES(?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, ticket.getId());
            preparedStatement.setLong(2, ticket.getType().getId());
            if(ticket instanceof SoldTicket)
                preparedStatement.setLong(3, ((SoldTicket) ticket).getPayment().getId());
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

    public Optional<TicketType> selectTypeById(Long typeId)
    {
        String selectSql = "SELECT * FROM TicketType WHERE typeId = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, typeId);
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

    public List<TicketType> selectAvailableTypes(Long eventId) {
        String selectSql = """
                select tt.typeId, tt.eventId, tt.type, tt.price\s
                from TicketType tt, Ticket t
                where tt.eventId = ? and t.typeId = tt.typeId
                group by tt.typeId, tt.eventId, tt.type, tt.price\s
                having count(t.ticketId) > 0;""";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketType> types = new ArrayList<>();
            Optional<TicketType> type = mapToTicketType(resultSet);
            while(type.isPresent())
            {
                types.add(type.get());
                type = mapToTicketType(resultSet);
            }
            return types;
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Optional<Ticket> selectTicketByType(Long typeId) {
        String selectSql = """
                select *
                from Ticket
                where typeId = ? and paymentId is null
                limit 1""";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, typeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToTicket(resultSet);
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<Ticket> mapToTicket(ResultSet resultSet) throws SQLException {
        if(resultSet.next())    {
            Long id = resultSet.getLong(1);
            Long typeId = resultSet.getLong(2);
            String clientId = resultSet.getString(3);
            if(clientId == null)
                return Optional.of(new Ticket(id, selectTypeById(typeId).get()));
        }
        return Optional.empty();
    }

    public void insertPayment(Payment payment) {
        String selectSql = "INSERT INTO Payment(paymentId, date, clientId) VALUES(?, ?, ?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        System.out.println(payment.getDate().getTime());
        System.out.println(new java.sql.Date(payment.getDate().getTime()));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, payment.getId());
            preparedStatement.setDate(2, new java.sql.Date(payment.getDate().getTime()));
            preparedStatement.setString(3, payment.getClient().getUsername());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }

    public void updateTicketPayment(Long ticketId, Long paymentId) {
        String selectSql = "UPDATE Ticket\n" +
                "SET PaymentId = ?\n" +
                "WHERE ticketId = ?;";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, paymentId);
            preparedStatement.setLong(2, ticketId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)    {
            e.printStackTrace();
        }
    }
}
