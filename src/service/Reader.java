package service;

import artist.Artist;

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

    public Map<String, Artist> readArtists() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("Data/Artist.csv"));
        scanner.useDelimiter("\n");
        scanner.nextLine();
        Map<String, Artist> artists = new HashMap<>();
        while (scanner.hasNext()){
            String line = scanner.next();
            String[] attributes = line.split(",");
            Artist artist = new Artist(Long.valueOf(attributes[0]), attributes[1], attributes[2]);
            artists.put(attributes[0], artist);
        }
        scanner.close();
        return artists;
    }
}
