package cli;

import commands.SqlCommand;
import engine.QueryExecutor;
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
        db = storageManager.load("src/main/java/storage/default_db.json");
        executor = new QueryExecutor(db);

        while (true) {
            System.out.print("db> ");
            String inputString = scanner.nextLine();
            if (inputString.equalsIgnoreCase("exit")) {
                StorageManager.save(db, "src/main/java/storage/default_db.json");
                break;
            }
            processInput(inputString);
        }
    }

    private void processInput(String input) {
        SqlCommand command = parser.getCommand(input);
        command.execute(executor);
    }
}
