package parser;

import commands.InsertCommand;
import commands.SqlCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlParser {
    private Tokenizer tokenizer = new Tokenizer();
    public SqlParser() {
    }

    public SqlCommand getCommand(String query) {
        List<String> queryTokens = new ArrayList<>(tokenizer.tokenize(query));
        if (queryTokens.get(0).equalsIgnoreCase("SELECT")) {
            return parseSelect(queryTokens);
        } else if (queryTokens.get(0).equalsIgnoreCase("INSERT")) {
            return parseInsert(queryTokens);
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

    private SqlCommand parseInsert(List<String> queryTokens) {
        queryTokens.remove(0);

        if (!queryTokens.get(0).equalsIgnoreCase("INTO")) {
            throw new RuntimeException("Expected INTO");
        }
        queryTokens.remove(0);

        String tableName = queryTokens.remove(0);

        List<String> columns = new ArrayList<>();
        if (queryTokens.get(0).equals("(")) {
            queryTokens.remove(0);

            while (!queryTokens.get(0).equals(")")) {
                if (queryTokens.get(0).equals(",")) {
                    queryTokens.remove(0);
                    continue;
                }
                columns.add(queryTokens.remove(0));
            }
            queryTokens.remove(0);
        }

        if (!queryTokens.get(0).equalsIgnoreCase("VALUES")) {
            throw new RuntimeException("Expected VALUES");
        }
        queryTokens.remove(0);

        List<String> values = new ArrayList<>();
        if (!queryTokens.get(0).equals("(")) {
            throw new RuntimeException("Expected ( after VALUES");
        }
        queryTokens.remove(0);

        while (!queryTokens.get(0).equals(")")) {
            if (queryTokens.get(0).equals(",")) {
                queryTokens.remove(0);
                continue;
            }
            values.add(queryTokens.remove(0));
        }
        queryTokens.remove(0);

        Map<String, String> valuesMap = new HashMap<>();
        for (String value : values) {
            valuesMap.put(columns.get(values.indexOf(value)), value);
        }

        return new InsertCommand(tableName, valuesMap);
    }
}