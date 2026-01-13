package parser;

import commands.InsertCommand;
import commands.SelectCommand;
import commands.SqlCommand;
import commands.WhereClause;
import exception.SqlSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlParser {
    private Tokenizer tokenizer = new Tokenizer();

    public SqlParser() {
    }

    public SqlCommand getCommand(String query) throws SqlSyntaxException {
        List<String> queryTokens = new ArrayList<>(tokenizer.tokenize(query));
        if (queryTokens.getFirst().equalsIgnoreCase("SELECT")) {
            return parseSelect(queryTokens);
        } else if (queryTokens.getFirst().equalsIgnoreCase("INSERT")) {
            return parseInsert(queryTokens);
        } else if (queryTokens.getFirst().equalsIgnoreCase("UPDATE")) {
            return parseUpdate(queryTokens);
        } else if (queryTokens.getFirst().equalsIgnoreCase("DELETE")) {
            return parseDelete(queryTokens);
        } else if (queryTokens.getFirst().equalsIgnoreCase("CREATE")) {
            return parseCreate(queryTokens);
        }
        return null;
    }

    private SqlCommand parseSelect(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();
        List<String> columns = new ArrayList<>();

        while (!queryTokens.getFirst().equalsIgnoreCase("FROM")) {
            if (queryTokens.getFirst().equals(",")) {
                queryTokens.removeFirst();
                continue;
            }
            columns.add(queryTokens.removeFirst());
        }
        queryTokens.removeFirst();
        String tableName = queryTokens.removeFirst();

        if (!queryTokens.isEmpty() && queryTokens.getFirst().equalsIgnoreCase("WHERE")) {
            WhereClause whereClause = parseWhereClause(queryTokens);
            return new SelectCommand(tableName, columns, whereClause);
        }

        return new SelectCommand(tableName, columns);
    }

    private SqlCommand parseInsert(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();

        if (!queryTokens.get(0).equalsIgnoreCase("INTO")) {
            throw new SqlSyntaxException("Expected INTO");
        }
        queryTokens.removeFirst();

        String tableName = queryTokens.removeFirst();

        List<String> columns = new ArrayList<>();
        if (queryTokens.getFirst().equals("(")) {
            queryTokens.removeFirst();

            while (!queryTokens.getFirst().equals(")")) {
                if (queryTokens.getFirst().equals(",")) {
                    queryTokens.removeFirst();
                    continue;
                }
                columns.add(queryTokens.removeFirst());
            }
            queryTokens.removeFirst();
        }

        if (!queryTokens.getFirst().equalsIgnoreCase("VALUES")) {
            throw new SqlSyntaxException("Expected VALUES");
        }
        queryTokens.removeFirst();

        List<String> values = new ArrayList<>();
        if (!queryTokens.getFirst().equals("(")) {
            throw new SqlSyntaxException("Expected ( after VALUES");
        }
        queryTokens.removeFirst();

        while (!queryTokens.getFirst().equals(")")) {
            if (queryTokens.getFirst().equals(",")) {
                queryTokens.removeFirst();
                continue;
            }
            values.add(queryTokens.removeFirst());
        }
        queryTokens.removeFirst();

        Map<String, String> valuesMap = new HashMap<>();
        for (String value : values) {
            valuesMap.put(columns.get(values.indexOf(value)), value);
        }

        return new InsertCommand(tableName, valuesMap);
    }

    private SqlCommand parseUpdate(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();

        String tableName = queryTokens.removeFirst();

        if (!queryTokens.getFirst().equalsIgnoreCase("SET")) {
            throw new SqlSyntaxException("Expected SET");
        }
        queryTokens.removeFirst();

        Map<String, String> newValues = new HashMap<>();

        while (!queryTokens.isEmpty()) {
            String column = queryTokens.removeFirst();

            if (!queryTokens.getFirst().equals("=")) {
                throw new SqlSyntaxException("Expected = after column name");
            }
            queryTokens.removeFirst();

            String value = queryTokens.removeFirst();
            newValues.put(column, value);

            if (!queryTokens.isEmpty() && queryTokens.getFirst().equals(",")) {
                queryTokens.removeFirst();
            } else {
                break;
            }
        }

        return new commands.UpdateCommand(tableName, newValues);
    }

    private SqlCommand parseDelete(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();

        if (!queryTokens.getFirst().equalsIgnoreCase("FROM")) {
            throw new SqlSyntaxException("Expected FROM");
        }
        queryTokens.removeFirst();

        String tableName = queryTokens.removeFirst();

        return new commands.DeleteCommand(tableName);
    }

    private SqlCommand parseCreate(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();

        if (!queryTokens.getFirst().equalsIgnoreCase("TABLE")) {
            throw new SqlSyntaxException("Expected TABLE");
        }
        queryTokens.removeFirst();

        String tableName = queryTokens.removeFirst();

        if (!queryTokens.getFirst().equals("(")) {
            throw new SqlSyntaxException("Expected '(' after table name");
        }
        queryTokens.removeFirst();

        List<String> columns = new ArrayList<>();
        while (!queryTokens.getFirst().equals(")")) {
            if (queryTokens.getFirst().equals(",")) {
                queryTokens.removeFirst();
                continue;
            }
            columns.add(queryTokens.removeFirst());
        }
        queryTokens.removeFirst();

        return new commands.CreateCommand(tableName, columns);
    }

    private WhereClause parseWhereClause(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.removeFirst();
        String whereColumn = queryTokens.removeFirst();
        if (!queryTokens.removeFirst().equalsIgnoreCase("=")) {
            throw new SqlSyntaxException("Expected = in WHERE clause");
        }
        String whereValue = queryTokens.removeFirst();
        return new WhereClause(whereColumn, whereValue);
    }
}
