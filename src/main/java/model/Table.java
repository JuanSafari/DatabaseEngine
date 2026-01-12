package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Table {
    private String name;
    private List<String> columns;
    private List<String[]> rows;

    public Table(String name) {
        this.name = name;
    }

    public void addRow(String[] row) {
        rows.add(row);
    }
}
