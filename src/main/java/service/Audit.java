package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Audit
{
    private static Audit instance;

    private Audit(){}

    public static Audit getInstance()
    {
        if (instance == null)
            instance = new Audit();
        return instance;
    }

    public void writeAudit(String action_name, LocalDateTime timestamp){
        try {
            FileWriter fileWriter = new FileWriter("Data/Audit.csv", true);
            fileWriter.write("\n");
            fileWriter.write(action_name);
            fileWriter.write(",");
            fileWriter.write(timestamp.toString());
            fileWriter.close();
        }
        catch (IOException exception)
        {
            System.out.println(exception.toString());
            return;
        }
    }




}