package commands;

import java.util.List;

import engine.QueryExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SelectCommand implements SqlCommand {
    private final String tableName;
    private final List<String> columns;

    public void execute(QueryExecutor executor) {
        executor.executeSelect(this);
    }
}
