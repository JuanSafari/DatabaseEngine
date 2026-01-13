package cli;

import commands.SqlCommand;
import engine.QueryExecutor;
import exception.MemoryException;
import exception.SqlSyntaxException;
import model.Database;
import parser.SqlParser;
import storage.StorageManager;

import java.util.Scanner;

public class CommandLineInterface {
    private SqlParser parser = new SqlParser();
    private QueryExecutor executor;
    private StorageManager storageManager = new StorageManager();
    private Scanner scanner = new Scanner(System.in);

    private Database db;

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.run();
    }

    private void run() {
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
                    StorageManager.save(db, "src/main/java/storage/default_db.json");
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
        } catch (RuntimeException e) {
            System.out.println("Error: Incorrect command");
        }
    }
}
