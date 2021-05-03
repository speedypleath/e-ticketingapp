package service;

import artist.Artist;
import user.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Writer
{
    private static Writer instance;

    private Writer(){}

    public static Writer getInstance()
    {
        if (instance == null)
            instance = new Writer();
        return instance;
    }

    public void writeArtists(Map<Long, Artist> artists) {
        try {
            File file = new File("Data/Artist.csv");
            FileWriter writer = new FileWriter(file, false);
            writer.write("id,name,pseudonym\n");
            for (Map.Entry<Long, Artist> artist : artists.entrySet()) {
                StringBuilder line = new StringBuilder();
                line.append(artist.getKey());
                line.append(',');
                line.append(artist.getValue().getName());
                line.append(',');
                line.append(artist.getValue().getPseudonym());
                line.append("\n");
                writer.write(line.toString());
            }
            writer.close();
        }
        catch (IOException exception)
        {
            System.out.println(exception.toString());
            return;
        }
    }

    public void writeUsers(Map<String, User> users)
    {
        try {
            File file = new File("Data/User.csv");
            FileWriter writer = new FileWriter(file, false);
            writer.write("username,password,salt,name,email\n");
            for (Map.Entry<String, User> user : users.entrySet()) {
                StringBuilder line = new StringBuilder();
                line.append(user.getKey());
                line.append(',');
                line.append(user.getValue().getPassword());
                line.append(',');
                line.append(user.getValue().getSalt());
                line.append(',');
                line.append(user.getValue().getName());
                line.append(',');
                line.append(user.getValue().getEmail());
                line.append("\n");
                writer.write(line.toString());
            }
            writer.close();
        }
        catch (IOException exception)
        {
            System.out.println(exception.toString());
            return;
        }
    }
}