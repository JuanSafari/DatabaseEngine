package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Database;

import java.io.File;
import java.io.IOException;

public class StorageManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void save(Database db, String path) {
        try {
            objectMapper.writeValue(new File(path), db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database load(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Database file not found. Creating a new database.");
                return new Database();
            }
            return objectMapper.readValue(file, Database.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Database();
        }
    }
}
