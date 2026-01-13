package commands;

import engine.QueryExecutor;
import exception.DatabaseException;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateCommand implements SqlCommand {
    private final String tableName;
    private final Map<String, String> newValues;

    public void execute(QueryExecutor executor) throws DatabaseException {
        executor.executeUpdate(this);
    }
}
