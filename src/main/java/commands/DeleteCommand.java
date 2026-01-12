package commands;

import lombok.Data;

@Data
public class DeleteCommand implements SqlCommand {
    private final String tableName;

    public void execute(engine.QueryExecutor executor) {
        executor.executeDelete(this);
    }
}
