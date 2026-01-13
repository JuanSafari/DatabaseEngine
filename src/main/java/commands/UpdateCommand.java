package commands;

import engine.QueryExecutor;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateCommand implements SqlCommand {
    private final String tableName;
    private final Map<String, String> newValues;

    public void execute(QueryExecutor executor) {
        executor.executeUpdate(this);
    }
}
