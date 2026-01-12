package commands;


import engine.QueryExecutor;

public interface SqlCommand {
    void execute(QueryExecutor executor);
}
