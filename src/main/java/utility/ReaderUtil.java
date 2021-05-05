package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public abstract class ReaderUtil<K, V> {

    protected abstract Map<K, V> newMap();
    protected abstract void doThings(String[] data, Map<K, V> resultMap);

    public Map<K, V> readLogsIntoMap(File file) {
        Scanner scanner = null;
        Map<K, V> resultMap = newMap();
        try {
            scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] data = line.split(",");
                doThings(data, resultMap);
            }
            scanner.close();
            return resultMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}