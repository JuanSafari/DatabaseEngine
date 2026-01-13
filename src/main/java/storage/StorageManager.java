package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.MemoryException;
import model.Database;

import java.io.File;
import java.io.IOException;

public class StorageManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void save(Database db, String path) throws MemoryException {
        try {
            objectMapper.writeValue(new File(path), db);
        } catch (Exception e) {
            throw new MemoryException("Error saving database: " + e.getMessage());
        }
    }

    public static Database load(String path) throws MemoryException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Database file not found. Creating a new database.");
                return new Database();
            }
            return objectMapper.readValue(file, Database.class);
        } catch (Exception e) {
            throw new MemoryException("Error loading database: " + e.getMessage());
        }
    }
}
