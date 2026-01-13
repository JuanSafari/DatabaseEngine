package commands;

import lombok.Data;

@Data
public class WhereClause {
    private final String whereColumn;
    private final String whereValue;
}
