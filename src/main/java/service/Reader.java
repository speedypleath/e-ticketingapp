package service;

import models.Artist;
import models.ActualEvent;
import models.Event;
import models.VirtualEvent;
import models.Location;
import repository.EventRepository;
import models.Administrator;
import models.Client;
import models.Organiser;
import models.User;
import utility.ReaderUtil;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Reader
{
    private static Reader instance;

    private Reader(){}

    public static Reader getInstance()
    {
        if (instance == null)
            instance = new Reader();
        return instance;
    }

    public Map<Long, Event> readEvents(Map<Long, Artist> artists, Map<String, User> users, Map<Long, Location> locations) {
        ReaderUtil<Long, Event> reader = new ReaderUtil<Long, Event>(){
            protected Map<Long, Event> newMap() {
                return new HashMap<Long, Event>();
            }
            protected void doThings(String[] data, Map<Long, Event> events) {
                Event.Builder builder = null;
                if(data[6].equals("online"))
                    builder = new VirtualEvent.Builder();
                else
                    builder = new ActualEvent.Builder();
                Event.Builder eventBuilder = builder;
                try {
                    eventBuilder
                            .id(Long.valueOf(data[0]))
                            .name(data[1])
                            .description(data[2])
                            .date(new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us")).parse(data[3]))
                            .organiser((Organiser) users.get(data[4]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Arrays.stream(data[5].split("/"))
                        .forEach((s ->
                                eventBuilder.addArtist(artists.get(Long.valueOf(s)))));
                if(data[6].equals("online")) {
                    VirtualEvent.Builder virtualBuilder = (VirtualEvent.Builder) builder;
                    virtualBuilder.inviteLink(data[7]);
                    VirtualEvent event = virtualBuilder.build();
                    events.put(event.getId(), event);
                }
                else {
                    ActualEvent.Builder liveBuilder = (ActualEvent.Builder) builder;
                    locations.forEach((aLong, location) -> {
                        if (location.equals(data[7]))
                            liveBuilder.location(location);
                    });
                    ActualEvent event = liveBuilder.build();
                    events.put(event.getId(), event);
                }
            }
        };
        return reader.readLogsIntoMap(new File("Data/Event.csv"));
    }

    public Map<Long, Artist> readArtists() {
        ReaderUtil<Long, Artist> reader = new ReaderUtil<Long, Artist>(){
            protected Map<Long, Artist> newMap() {
                return new HashMap<Long, Artist>();
            }
            protected void doThings(String[] data, Map<Long, Artist> artists){
                Artist artist = new Artist(Long.valueOf(data[0]), data[1], data[2]);
                artists.put(Long.valueOf(data[0]), artist);
            }

        };
        return reader.readLogsIntoMap(new File("Data/Artist.csv"));
    }

    public Map<String, User> readUsers() {
        ReaderUtil<String, User> reader = new ReaderUtil<String, User>(){
            protected Map<String, User> newMap() {
                return new HashMap<String, User>();
            }
            protected void doThings(String[] data, Map<String, User> users){
                User user = null;
                if(data[5].equals("client"))
                    user = new Client(data[2], data[0], data[1], data[4], data[3]);
                else if(data[5].equals("administrator"))
                    user = new Administrator(data[2], data[0], data[1], data[4], data[3]);
                else if(data[5].equals("organiser"))
                    user = new Organiser(data[2], data[0], data[1], data[4], data[3]);
                users.put(data[0], user);
            }
        };
        return reader.readLogsIntoMap(new File("Data/User.csv"));
    }

    public Map<Long, Location> readLocations() {
        ReaderUtil<Long, Location> reader = new ReaderUtil<Long, Location>(){
            protected Map<Long, Location> newMap() {
                return new HashMap<Long, Location>();
            }
            protected void doThings(String[] data, Map<Long, Location> locations){
                    locations.put(Long.valueOf(data[0]),
                            new Location(Long.valueOf(data[0]), data[1], data[2], Integer.valueOf(data[3])));
                }

        };
        return reader.readLogsIntoMap(new File("Data/Location.csv"));
    }

    public Map<Long, Event> readEventsFromDatabase() {
        EventRepository eventRepository = new EventRepository();
        return eventRepository.selectAllAsMap().get();
    }
}
