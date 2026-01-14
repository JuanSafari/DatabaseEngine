package commands;

import engine.QueryExecutor;
import exception.DatabaseException;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateCommand implements SqlCommand {
    private final String tableName;
    private final Map<String, String> newValues;
    private WhereClause whereClause;

    public UpdateCommand(String tableName, Map<String, String> newValues) {
        this.tableName = tableName;
        this.newValues = newValues;
    }

    public UpdateCommand(String tableName, Map<String, String> newValues, WhereClause whereClause) {
        this.tableName = tableName;
        this.newValues = newValues;
        this.whereClause = whereClause;
    }

    public void execute(QueryExecutor executor) throws DatabaseException {
        executor.executeUpdate(this);
    }
}
