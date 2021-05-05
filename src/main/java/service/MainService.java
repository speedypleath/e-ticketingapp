package service;

import artist.Artist;
import event.ActualEvent;
import event.Event;
import event.VirtualEvent;
import location.Location;
import user.Administrator;
import user.Client;
import user.Organiser;
import user.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.*;

public class MainService {
    private User user;
    private final static Map<Long, Artist> artists;
    private final static Map<String, User> users;
    private final static Map<Long, Location> locations;
    private final static Map<Long, Event> events;
    private static MainService instance;
    private MainService(){}

    static {
        Reader reader = Reader.getInstance();
        locations = reader.readLocations();
        artists = reader.readArtists();
        users = reader.readUsers();
        events = reader.readEvents(artists, users, locations);
    }

    public static MainService getInstance()
    {
        if(instance == null)
            instance = new MainService();
        return instance;
    }
    public StringBuilder register(String username, String password, String email, String name, String role){
        System.out.println("Da");
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        StringBuilder result = new StringBuilder();

        if(!Validator.validateName(name)){
            result.append("Name should start with an uppercase character\n");
        }
        if(!Validator.validateEmail(email)) {
            result.append("Email is not valid\n");
        }
        if(!Validator.validatePassword(password))   {
            result.append("Your password must have at least 8 characters long, 1 uppercase & 1 lowercase character, 1 number, 1 special character");
        }

        if(result.length() == 0) {
            Audit.getInstance().writeAudit("User registered", LocalDateTime.now());
            User user = switch (role) {
                case "client" -> new Client(salt, username, encryptedPassword, email, name);
                case "administrator" -> new Administrator(salt, username, encryptedPassword, email, name);
                case "organiser" -> new Organiser(salt, username, encryptedPassword, email, name);
                default -> null;
            };
            users.put(username, user);
            System.out.println(user);
        }
        else
            System.out.println(result);
        return result;
    }

    // Get a encrypted password using PBKDF2 hash algorithm
    public String getEncryptedPassword(String password, String salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 20000; // NIST specifies 10000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);

        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
            byte[] encBytes = f.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(encBytes);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public String getNewSalt(){
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[8];
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public boolean login(String username, String password){
        User user = users.get(username);
        if (user == null) {
            return false;
        } else {
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(password, salt);
            if (calculatedHash.equals(user.getPassword())){
                this.user = user;
                Audit.getInstance().writeAudit("User logged in", LocalDateTime.now());
                return true;
            } else {
                return false;
            }
        }
    }

    void logout()
    {
        this.user = null;
    }

    public void addEvent(String name, String description, Date date, List<String> artistStrings, String type, String locationName)
    {
        Event.Builder builder = null;
        if(type.equals("online"))
            builder = new VirtualEvent.Builder();
        else
            builder = new ActualEvent.Builder();
        Event.Builder eventBuilder = builder;
        eventBuilder.name(name)
                .description(description)
                .date(date)
                .organiser((Organiser) user);

        artists.forEach((aLong, artist) -> {
            if(artistStrings.contains(artist.getPseudonym()))
                eventBuilder.addArtist(artist);
        });
        if(type.equals("online")) {
            VirtualEvent.Builder virtualBuilder = (VirtualEvent.Builder) builder;
            virtualBuilder.inviteLink(locationName);
            VirtualEvent event = virtualBuilder.build();
            events.put(event.getId(), event);
        }
        else {
            ActualEvent.Builder liveBuilder = (ActualEvent.Builder) builder;
            locations.forEach((aLong, location) -> {
                if (location.equals(locationName))
                    liveBuilder.location(location);
            });
            ActualEvent event = liveBuilder.build();
            events.put(event.getId(), event);
        }
        Audit.getInstance().writeAudit("Event added", LocalDateTime.now());
    }

    public void addArtist(String name, String pseudonym)
    {
        Artist artist = new Artist(name, pseudonym);
        artist.addToMap(artists);
        Audit.getInstance().writeAudit("Artist added", LocalDateTime.now());
    }

    public boolean addLocation(String name, String address, String capacity)
    {
        try {
            Integer c = Integer.parseInt(capacity);
            Location location = new Location(name, address, c);
            locations.put(location.getId(), location);
            Audit.getInstance().writeAudit("Location added", LocalDateTime.now());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    void deleteEvent(Event event)
    {

    }

    void updateEvent(Event event)
    {

    }

    public Vector<String> getArtists(){
        Vector<String> artistVector = new Vector<>();
        artists.forEach(((aLong, artist) -> artistVector.add(artist.getPseudonym())));
        return artistVector;
    }

    public Vector<String> getLocations(){
        Vector<String> locationVector = new Vector<>();
        artists.forEach(((aLong, location) -> locationVector.add(location.getName())));
        return locationVector;
    }

    public Vector<String> getEvents(){
        Vector<String> eventVector = new Vector<>();
        events.forEach(((aLong, event) -> eventVector.add(event.getName() + "   " + event.getDate().toString())));
        return eventVector;
    }

    public void saveData()
    {
        Writer writer = Writer.getInstance();
        writer.writeLogsIntoMap(new File("Data/Location.csv"), locations);
        writer.writeLogsIntoMap(new File("Data/Artist.csv"), artists);
        writer.writeLogsIntoMap(new File("Data/User.csv"), users);
        writer.writeLogsIntoMap(new File("Data/Event.csv"), events);
    }
}
