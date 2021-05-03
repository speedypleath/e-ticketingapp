package service;

import artist.Artist;
import user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("Data/Artist.csv"));
            scanner.useDelimiter("\n");
            scanner.nextLine();
            Map<Long, Artist> artists = new HashMap<>();
            while (scanner.hasNext()){
                String line = scanner.next();
                String[] attributes = line.split(",");
                Artist artist = new Artist(Long.valueOf(attributes[0]), attributes[1], attributes[2]);
                artists.put(Long.valueOf(attributes[0]), artist);
            }
            scanner.close();
            return artists;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, User> readUsers() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("Data/User.csv"));
            scanner.useDelimiter("\n");
            scanner.nextLine();
            Map<String, User> users = new HashMap<>();
            while (scanner.hasNext()){
                String line = scanner.next();
                String[] attributes = line.split(",");
                User user = new User(attributes[2], attributes[0], attributes[1], attributes[4], attributes[3]);
                users.put(attributes[0], user);
            }
            scanner.close();
            return users;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
