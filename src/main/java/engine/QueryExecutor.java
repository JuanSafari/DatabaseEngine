package engine;

import commands.InsertCommand;
import commands.SelectCommand;
import lombok.Data;
import model.Database;
import model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class QueryExecutor {
    private final Database database;

    public void executeSelect(SelectCommand command) {
        Table table = database.getTable(command.getTableName());
        if (table == null) {
            throw new RuntimeException("Table not found: " + command.getTableName());
        }

        List<String> tableColumns = table.getColumns();
        List<String[]> tableRows = table.getRows();

        List<Integer> selectedIndexes = new ArrayList<>();
        if (command.getColumns().get(0).equals("*")) {
            for (int i = 0; i < tableColumns.size(); i++) {
                selectedIndexes.add(i);
            }
        } else {
            for (String col : command.getColumns()) {
                int index = tableColumns.indexOf(col);
                if (index == -1) {
                    throw new RuntimeException("Column not found: " + col);
                }
                selectedIndexes.add(index);
            }
        }

        for (int index : selectedIndexes) {
            System.out.print(tableColumns.get(index) + " | ");
        }
        System.out.println();

        for (String[] row : tableRows) {
            for (int index : selectedIndexes) {
                System.out.print(row[index] + " | ");
            }
            System.out.println();
        }
    }

    public void executeInsert(InsertCommand command) {
        Table table = database.getTable(command.getTableName());
        if (table == null) {
            throw new RuntimeException("Table not found: " + command.getTableName());
        }

        List<String> tableColumns = table.getColumns();

        String[] newRow = new String[tableColumns.size()];

        for (Map.Entry<String, String> entry : command.getValues().entrySet()) {
            String columnName = entry.getKey();
            String value = entry.getValue();

            int index = tableColumns.indexOf(columnName);
            if (index == -1) {
                throw new RuntimeException("Column not found: " + columnName);
            }

            newRow[index] = value;
        }

        table.addRow(newRow);
    }

    public void executeUpdate(Object command) {
        System.out.println("Executing UPDATE command");
    }

    public void executeDelete(Object command) {
        System.out.println("Executing DELETE command");
    }
}
