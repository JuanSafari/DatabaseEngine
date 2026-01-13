package commands;

import exception.DatabaseException;
import lombok.Data;

@Data
public class DeleteCommand implements SqlCommand {
    private final String tableName;

    public void execute(engine.QueryExecutor executor) throws DatabaseException {
        executor.executeDelete(this);
    }
}
