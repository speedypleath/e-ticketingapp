package service;

import repository.UserRepository;
import user.Administrator;
import user.Client;
import user.User;
import utility.CSV;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Scanner;

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
/*
    public void writeUsers(Map<String, User> users)
    {
        try {
            File file = new File("Data/User.csv");
            FileWriter writer = new FileWriter(file, false);
            writer.write("username,password,salt,name,email,role\n");
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
                line.append(',');
                line.append(user.getValue().getClass().getSimpleName().toLowerCase(Locale.ROOT));
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
    } */
    public <K, T> void writeLogsIntoMap(File file, Map<K, T> logs) {
        try {
            Scanner reader = new Scanner(file);
            reader.useDelimiter("\n");
            String firstLine = reader.nextLine();
            reader.close();
            FileWriter writer = new FileWriter(file, false);
            writer.write(firstLine + '\n');
            for (Map.Entry<K, T> log : logs.entrySet())
                writer.write(((CSV)log.getValue()).toCSV());
            writer.close();
        }
        catch (IOException exception)
        {
            System.out.println(exception.toString());
            return;
        }
    }

    public void writeUsersIntoDatabase(Map<String, User> users) {
        UserRepository userRepository = new UserRepository();
        users.forEach((s, user) -> {
            if(!userRepository.getByUsername(user.getUsername()).isPresent()) {
                String role;
                if (user instanceof Administrator)
                    role = "administrator";
                else if (user instanceof Client)
                    role = "client";
                else
                    role = "organiser";
                try {
                    userRepository.insert(user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getSalt(), role);
                } catch (SQLIntegrityConstraintViolationException throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}