package service;

import models.*;
import auth.AuthActions;
import exceptions.NoOrganiserException;
import exceptions.NoRoleException;
import exceptions.NoTypeException;
import repository.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MainService implements AuthActions {
    AuthActions actions;
    private User user;
    private final static Map<Long, Artist> artists;
    private final static Map<String, User> users;
    private final static Map<Long, Location> locations;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository = new ArtistRepository();
    private final EventRepository eventRepository = new EventRepository();
    private final LocationRepository locationRepository = new LocationRepository();
    private final TicketRepository ticketRepository = new TicketRepository();
    private static Map<Long, Event> events;
    private static MainService instance;
    private final Set<Event> shoppingCart = new HashSet<>();

    private MainService() {
        userRepository = new UserRepository();
    }

    static {
        Reader reader = Reader.getInstance();
        locations = reader.readLocations();
        artists = reader.readArtists();
        users = reader.readUsers();
        events = reader.readEventsFromDatabase();
    }

    public static MainService getInstance() {
        if (instance == null)
            instance = new MainService();
        return instance;
    }

    public StringBuilder register(String username, String password, String email, String name, String role) throws NoRoleException {
        System.out.println("Da");
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        StringBuilder result = new StringBuilder();

        if(!role.equals("client") && !role.equals("organiser"))
            throw new NoRoleException();

        if (!Validator.validateEmail(email)) {
            result.append("Email is not valid\n");
        }
        if (!Validator.validatePassword(password)) {
            result.append("Your password must have at least 8 characters long, 1 uppercase & 1 lowercase character, 1 number, 1 special character");
        }
        try {
            userRepository.insert(username, name, email, encryptedPassword, salt, role);
        }
        catch (SQLIntegrityConstraintViolationException e) {
            result.append("username is taken");

        }
        Audit.getInstance().writeAudit("User registered", LocalDateTime.now());
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
            e.printStackTrace();
            return null;
        }
    }

    public String getNewSalt() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[8];
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean login(String username, String password) {
        Optional<User> checkUser = userRepository.getByUsername(username);
        if (checkUser.isEmpty()) {
            return false;
        } else {
            User user = checkUser.get();
            System.out.println(user);
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(password, salt);
            if (calculatedHash.equals(user.getPassword())) {
                this.user = user;
                Audit.getInstance().writeAudit("User logged in", LocalDateTime.now());
                actions.login(username, password);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void setNextAction(AuthActions nextAction) {
        this.actions = nextAction;
    }

    public void logout() {
        this.user = null;
        this.actions.logout();
    }

    public void addEvent(String name, String description, Date date, List<Artist> artists, String type, Location location,String inviteLink,  Long id) throws NoOrganiserException, NoTypeException {
        Event.Builder builder;
        if(!(user instanceof Organiser))
            throw new NoOrganiserException();
        if (type.equals("online"))
            builder = new VirtualEvent.Builder();
        else
            builder = new ActualEvent.Builder();
        Event.Builder eventBuilder = builder;
        eventBuilder.name(name)
                .description(description)
                .date(date)
                .organiser((Organiser) user);

        artists.forEach(eventBuilder::addArtist);

        if (id != null)
            eventBuilder.id(id);

        if (type.equals("online")) {
            VirtualEvent.Builder virtualBuilder = (VirtualEvent.Builder) builder;
            virtualBuilder.inviteLink(inviteLink);
            VirtualEvent event = virtualBuilder.build();
            eventRepository.insert(event.getId(), name, description, new java.sql.Date(date.getTime()), user.getUsername(), null, inviteLink, "virtual");
            events.put(event.getId(), event);
        } else if (type.equals("live")){
            ActualEvent.Builder liveBuilder = (ActualEvent.Builder) builder;
            liveBuilder.location(location);
            ActualEvent event = liveBuilder.build();
            eventRepository.insert(event.getId(), name, description, new java.sql.Date(date.getTime()), user.getUsername(), location.getId(), null, "actual");
            events.put(event.getId(), event);
        } else
            throw new NoTypeException();
        Audit.getInstance().writeAudit("Event added", LocalDateTime.now());
    }

    public void addArtist(String name, String pseudonym) {
        Artist artist = new Artist(name, pseudonym);
        artistRepository.insert(artist.getId(), name, pseudonym);
        artist.addToMap(artists);
        Audit.getInstance().writeAudit("Artist added", LocalDateTime.now());
    }

    public boolean addLocation(String name, String address, String capacity) {
        try {
            Integer c = Integer.parseInt(capacity);
            Location location = new Location(name, address, c);
            locationRepository.insert(location.getId(), name, address, c);
            locations.put(location.getId(), location);
            Audit.getInstance().writeAudit("Location added", LocalDateTime.now());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void deleteEvent(Event event) {
        events = events.entrySet().stream().filter(
                longEventEntry -> event != longEventEntry.getValue()
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        eventRepository.deleteEvent(event.getId());
        Audit.getInstance().writeAudit("Event deleted", LocalDateTime.now());
    }

    public void editEvent(Event event, String name, String description, Date date, List<Artist> artists, String type, Location location, String inviteLink) {
        deleteEvent(event);
        addEvent(name, description, date, artists, type, location, inviteLink, event.getId());
        Audit.getInstance().writeAudit("Event edited", LocalDateTime.now());
    }

    public String getUserRole() {
        if (user == null)
            return "not authenticated";
        if (user instanceof Administrator)
            return "admin";
        if (user instanceof Client)
            return "client";
        return "organiser";
    }

    public List<Artist> getArtists() {
        return artistRepository.selectAll();
    }

    public List<Location> getLocations() {
        return locationRepository.selectAll();
    }

    public List<Event> getEvents() {
        return eventRepository.selectAll();
    }

    public void saveData() {
        Writer writer = Writer.getInstance();
        writer.writeLogsIntoMap(new File("Data/Location.csv"), locations);
        writer.writeLogsIntoMap(new File("Data/Artist.csv"), artists);
        writer.writeLogsIntoMap(new File("Data/User.csv"), users);
        writer.writeLogsIntoMap(new File("Data/Event.csv"), events);
        writer.writeUsersIntoDatabase(users);
    }

    public List<Event> getUserEvents() {
        return eventRepository.selectAll().stream().filter(event -> event.getOrganiser().getUsername().equals(user.getUsername())).collect(Collectors.toList());
    }

    public List<Event> getEventsByArtist(Artist artist) {
        return events.values().stream()
                .filter(event -> event.getArtists().contains(artist))
                .collect(Collectors.toList());
    }

    public List<Event> getEventsByLocation(Location location) {
        return events.values().stream()
                .filter(event -> {
                    if(event instanceof ActualEvent)
                        return ((ActualEvent) event).getLocation().equals(location);
                    return false;
                })
                .collect(Collectors.toList());
    }

    public void deleteArtist(Artist artist) {
        GenericStatements.delete(artist.getId(), "Artist");
    }

    public void deleteLocation(Location location) {
        GenericStatements.delete(location.getId(), "Location");
    }

    public void editArtist(Artist artist, String name, String pseudonym)
    {
        artistRepository.deleteArtist(artist.getId());
        artistRepository.insert(artist.getId(), name, pseudonym);
    }

    public void editLocation(Location location, String name, String address, Integer capacity)
    {
        locationRepository.deleteLocation(location.getId());
        locationRepository.insert(location.getId(), name, address, capacity);
        locationRepository.insert(location.getId(), name, address, capacity);
    }

    public void addToCart(Event event)
    {
        shoppingCart.add(event);
    }

    public void createTickets(Event event, Integer no, Integer price, String type){
        TicketType ticketType = new TicketType(price, type, event);
        ticketRepository.insertType(ticketType);
        for(int i = 0; i < no; i++)
            ticketRepository.insert(new Ticket(ticketType));
    }
}
