package service;

import artist.Artist;
import location.Location;

import java.util.Scanner;

public class DataService
{
    private final static Scanner scanner = new Scanner(System.in);
    private DataService(){}

    public static Artist readArtist(){
        Artist artist = new Artist();
        System.out.println("Name: ");
        artist.setName(scanner.next());
        System.out.println("Pseudonym: ");
        artist.setPseudonym(scanner.next());
        return artist;
    }

    public static Location readLocation(){
        Location location = new Location();
        System.out.println("Name: ");
        location.setName(scanner.next());
        System.out.println("Capacity: ");
        location.setCapacity(Integer.valueOf(scanner.next()));
        System.out.println("Address");
        location.setAddress(scanner.next());
        return location;
    }
}
