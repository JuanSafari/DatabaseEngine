package commands;

import java.util.List;

import engine.QueryExecutor;
import exception.DatabaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class SelectCommand implements SqlCommand {
    private final String tableName;
    private final List<String> columns;
    private WhereClause whereClause;

    public SelectCommand(String tableName, List<String> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public SelectCommand(String tableName, List<String> columns, WhereClause whereClause) {
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = whereClause;
    }

    public void execute(QueryExecutor executor) throws DatabaseException {
        executor.executeSelect(this);
    }
}
