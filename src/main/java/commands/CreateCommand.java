package commands;

import engine.QueryExecutor;

import java.util.List;

import exception.DatabaseException;
import lombok.Data;

@Data
public class CreateCommand implements SqlCommand {
    private final String tableName;
    private final List<String> columns;

    @Override
    public void execute(QueryExecutor executor) throws DatabaseException {
        executor.executeCreate(this);
    }
}
