package commands;

import exception.DatabaseException;
import lombok.Data;

@Data
public class DeleteCommand implements SqlCommand {
    private final String tableName;
    private WhereClause whereClause;

    public DeleteCommand(String tableName) {
        this.tableName = tableName;
    }

    public DeleteCommand(String tableName, WhereClause whereClause) {
        this.tableName = tableName;
        this.whereClause = whereClause;
    }

    public void execute(engine.QueryExecutor executor) throws DatabaseException {
        executor.executeDelete(this);
    }
}
