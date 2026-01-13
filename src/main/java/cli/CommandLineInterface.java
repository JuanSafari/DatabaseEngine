package cli;

import commands.SqlCommand;
import engine.QueryExecutor;
import exception.DatabaseException;
import exception.MemoryException;
import exception.SqlSyntaxException;
import model.Database;
import parser.SqlParser;
import storage.StorageManager;

import java.util.Scanner;

public class CommandLineInterface {
    private final SqlParser parser = new SqlParser();
    private QueryExecutor executor;
    private final StorageManager storageManager = new StorageManager();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run();
    }

    private void run() {
        Database db;
        try {
            db = storageManager.load("src/main/java/storage/default_db.json");
        } catch (MemoryException e) {
            System.out.println("Error loading database: " + e.getMessage());
            System.out.println("Creating a new database.");
            db = new Database();
        }
        executor = new QueryExecutor(db);

        while (true) {
            System.out.print("db> ");
            String inputString = scanner.nextLine();
            if (inputString.equalsIgnoreCase("exit")) {
                try {
                    storageManager.save(db, "src/main/java/storage/default_db.json");
                } catch (MemoryException e) {
                    System.out.println("Error saving database: " + e.getMessage());
                }
                break;
            }
            processInput(inputString);
        }
    }

    private void processInput(String input) {
        try {
            SqlCommand command = parser.getCommand(input);
            command.execute(executor);
        } catch (SqlSyntaxException e) {
            System.out.println("Sql syntax error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
