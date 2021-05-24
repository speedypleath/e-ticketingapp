package repository;

import artist.Artist;
import config.DatabaseConfig;
import event.ActualEvent;
import event.Event;
import event.VirtualEvent;
import exceptions.NoOrganiserException;
import location.Location;
import user.Organiser;
import user.User;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class EventRepository {
    public void insert(Long id, String name, String description, Date date, String organiserId, Long locationId, String inviteLink, String eventType) {
        String insertSql = "INSERT INTO Event(eventId, name, description, date, organiserId, locationId, inviteLink, eventType) VALUES(?, ?, ?, ?, ?, ?, ? ,?)";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        System.out.println(id + name + description + date + organiserId + locationId + inviteLink + eventType);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setDate(4, new java.sql.Date(date.getTime()));
            preparedStatement.setString(5, organiserId);
            if (locationId == null)
                preparedStatement.setNull(6, Types.BIGINT);
            else
                preparedStatement.setLong(6, locationId);
            if (inviteLink == null)
                preparedStatement.setNull(7, Types.VARCHAR);
            else
                preparedStatement.setString(7, inviteLink);
            preparedStatement.setString(8, eventType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Event> getById(Long id) {
        String selectSql = "SELECT * FROM Event u WHERE u.eventId = ?";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToEvent(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<Event> mapToEvent(ResultSet resultSet) throws SQLException {
        try {
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String description = resultSet.getString(3);
                Date date = resultSet.getDate(4);
                String organiserId = resultSet.getString(5);
                Long locationId = resultSet.getLong(6);
                String inviteLink = resultSet.getString(7);
                String eventType = resultSet.getString(8);
                Event.Builder builder;
                UserRepository userRepository = new UserRepository();
                if (eventType.equals("virtual"))
                    builder = new VirtualEvent.Builder();
                else
                    builder = new ActualEvent.Builder();
                Event.Builder eventBuilder = builder;
                Optional<User> organiser = userRepository.getByUsername(organiserId);
                if(organiser.isPresent())
                    eventBuilder.organiser((Organiser) organiser.get());
                else
                    throw new NoOrganiserException();
                eventBuilder.name(name)
                        .description(description)
                        .date(date)
                        .id(id);

                ArtistEventRepository artistEventRepository = new ArtistEventRepository();
                Set<Artist> artists = artistEventRepository.getArtistsByEvent(id);
                artists.forEach((artist) -> {
                    eventBuilder.addArtist(artist);
                });
                Event event;
                if (eventType.equals("virtual")) {
                    VirtualEvent.Builder virtualBuilder = (VirtualEvent.Builder) builder;
                    virtualBuilder.inviteLink(inviteLink);
                    event = virtualBuilder.build();
                } else {
                    ActualEvent.Builder liveBuilder = (ActualEvent.Builder) builder;
                    LocationRepository locationRepository = new LocationRepository();;
                    Location location = locationRepository.getById(locationId).get();
                    liveBuilder.location(location);
                    event = liveBuilder.build();
                }
                return Optional.of(event);
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void deleteEvent(Long id) {
        GenericStatements.delete(id, "Event");
    }

    public Optional<Map<Long, Event>> selectAllAsMap()
    {
        String selectSql = "SELECT * FROM Event u";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        Map<Long, Event> events = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Event> event = mapToEvent(resultSet);
            while(!event.isEmpty())
            {
                events.put(event.get().getId(), event.get());
                event = mapToEvent(resultSet);
                System.out.println(event);
            }
            return Optional.of(events);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Vector<Event> selectAll()
    {
        String selectSql = "SELECT * FROM Event";
        Connection connection = DatabaseConfig.getDatabaseConnection();
        Vector<Event> events = new Vector<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Event> event = mapToEvent(resultSet);
            while(!event.isEmpty())
            {
                events.add(event.get());
                event = mapToEvent(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

}
