package service;

import artist.Artist;
import location.Location;
import user.Administrator;
import user.Client;
import user.Organiser;
import user.User;
import utility.ReaderUtil;

import java.io.File;
import java.util.HashMap;
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

}
