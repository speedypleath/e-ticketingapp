package service;

import artist.Artist;
import event.Event;
import location.Location;
import user.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MainService {
    private User user;
    private static Map<Long, Artist> artists;
    private static Map<String, User> users;
    private static Map<Long, Location> locations;
    private static Map<Long, Event> events;
    private static MainService instance;
    private MainService(){}

    static {
        locations = new HashMap<>();
        events = new HashMap<>();
        Reader reader = Reader.getInstance();
        artists = reader.readArtists();
        users = reader.readUsers();
        users.forEach(((s, user1) -> System.out.println(user1)));
    }

    public static MainService getInstance()
    {
        if(instance == null)
            instance = new MainService();
        return instance;
    }
    public void register(String username, String password, String email, String name){
        System.out.println("Da");
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        User user = new User(salt, username, encryptedPassword, email, name);
        users.put(username, user);
        System.out.println(user);
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

    public void addArtist()
    {
        Artist artist = DataService.readArtist();
        artist.addToMap(artists);
    }

    static  {
        artists = new HashMap<>();
        Artist artist1 = new Artist("Zheani Sparkes", "Zheani");
        Artist artist2 = new Artist("Claire Elise Boucher", "Grimes");
        Artist artist3 = new Artist("Katherine Mariko Zhang", "Lil Mariko");
        artist1.addToMap(artists);
        artist2.addToMap(artists);
        artist3.addToMap(artists);
    }

    void createEvent()
    {

    }

    void deleteEvent(Event event)
    {

    }

    void updateEvent(Event event)
    {

    }

    public void saveData()
    {
        Writer writer = Writer.getInstance();
        writer.writeArtists(artists);
        writer.writeUsers(users);
    }
}
