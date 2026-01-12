package parser;

import commands.SqlCommand;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
    private Tokenizer tokenizer = new Tokenizer();
    public SqlParser() {
    }

    public SqlCommand getCommand(String query) {
        List<String> queryTokens = new ArrayList<>(tokenizer.tokenize(query));
        if (queryTokens.get(0).equalsIgnoreCase("SELECT")) {
            return parseSelect(queryTokens);
        }
        return null;
    }

    private SqlCommand parseSelect(List<String> queryTokens) {
        queryTokens.remove(0);
        List<String> columns = new ArrayList<>();
        while(!queryTokens.get(0).equalsIgnoreCase("FROM")) {
            if (queryTokens.get(0).equals(",")) {
                queryTokens.remove(0);
                continue;
            }
            columns.add(queryTokens.remove(0));
        }
        queryTokens.remove(0);
        String tableName = queryTokens.remove(0);
        return new commands.SelectCommand(tableName, columns);
    }
}
