package model;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, Table> tables = new HashMap<>();

    public Map<String, Table> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        tables.put(table.getName(), table);
    }

    public Table getTable(String name) {
        return tables.get(name);
    }
}
