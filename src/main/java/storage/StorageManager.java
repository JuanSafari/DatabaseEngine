package storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.MemoryException;
import model.Database;

import java.io.File;

public class StorageManager {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(Database db, String path) throws MemoryException {
        try {
            objectMapper.writeValue(new File(path), db);
        } catch (Exception e) {
            throw new MemoryException("Error saving database: " + e.getMessage());
        }
    }

    public Database load(String path) throws MemoryException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                throw new MemoryException("Database file not found at path: " + path);
            }
            return objectMapper.readValue(file, Database.class);
        } catch (Exception e) {
            throw new MemoryException("Error loading database: " + e.getMessage());
        }
    }
}
