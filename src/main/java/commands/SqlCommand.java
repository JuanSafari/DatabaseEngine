package commands;


import engine.QueryExecutor;
import exception.DatabaseException;

public interface SqlCommand {
    void execute(QueryExecutor executor) throws DatabaseException;
}
