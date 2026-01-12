package commands;

import engine.QueryExecutor;
import lombok.Data;

import java.util.Map;

@Data
public class InsertCommand implements SqlCommand {
    private final String tableName;
    private final Map<String, String> values;

    public void execute(QueryExecutor executor) {
        executor.executeInsert(this);
    }
}
