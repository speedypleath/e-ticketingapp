package utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class WriterUtil <T> {
    public <K> void writeLogsIntoMap(File file, Map<K, T> logs) {
        try {
            Scanner reader = new Scanner(file);
            reader.useDelimiter("\n");
            String firstLine = reader.nextLine();
            reader.close();
            FileWriter writer = new FileWriter(file, false);
            writer.write(firstLine);
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
}
