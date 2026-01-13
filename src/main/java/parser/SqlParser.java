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
        if (queryTokens.get(0).equalsIgnoreCase("SELECT")) {
            return parseSelect(queryTokens);
        } else if (queryTokens.get(0).equalsIgnoreCase("INSERT")) {
            return parseInsert(queryTokens);
        } else if (queryTokens.get(0).equalsIgnoreCase("UPDATE")) {
            return parseUpdate(queryTokens);
        } else if (queryTokens.get(0).equalsIgnoreCase("DELETE")) {
            return parseDelete(queryTokens);
        } else if (queryTokens.get(0).equalsIgnoreCase("CREATE")) {
            return parseCreate(queryTokens);
        }
        return null;
    }

    private SqlCommand parseSelect(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);
        List<String> columns = new ArrayList<>();
        while (!queryTokens.get(0).equalsIgnoreCase("FROM")) {
            if (queryTokens.get(0).equals(",")) {
                queryTokens.remove(0);
                continue;
            }
            columns.add(queryTokens.remove(0));
        }
        queryTokens.remove(0);
        String tableName = queryTokens.remove(0);

        if (!queryTokens.isEmpty() && queryTokens.get(0).equalsIgnoreCase("WHERE")) {
            WhereClause whereClause = parseWhereClause(queryTokens);
            return new SelectCommand(tableName, columns, whereClause);
        }

        return new SelectCommand(tableName, columns);
    }

    private SqlCommand parseInsert(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);

        if (!queryTokens.get(0).equalsIgnoreCase("INTO")) {
            throw new SqlSyntaxException("Expected INTO");
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
            throw new SqlSyntaxException("Expected VALUES");
        }
        queryTokens.remove(0);

        List<String> values = new ArrayList<>();
        if (!queryTokens.get(0).equals("(")) {
            throw new SqlSyntaxException("Expected ( after VALUES");
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

    private SqlCommand parseUpdate(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);

        String tableName = queryTokens.remove(0);

        if (!queryTokens.get(0).equalsIgnoreCase("SET")) {
            throw new SqlSyntaxException("Expected SET");
        }
        queryTokens.remove(0);

        Map<String, String> newValues = new HashMap<>();

        while (!queryTokens.isEmpty()) {
            String column = queryTokens.remove(0);

            if (!queryTokens.get(0).equals("=")) {
                throw new SqlSyntaxException("Expected = after column name");
            }
            queryTokens.remove(0);

            String value = queryTokens.remove(0);
            newValues.put(column, value);

            if (!queryTokens.isEmpty() && queryTokens.get(0).equals(",")) {
                queryTokens.remove(0);
            } else {
                break;
            }
        }

        return new commands.UpdateCommand(tableName, newValues);
    }

    private SqlCommand parseDelete(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);

        if (!queryTokens.get(0).equalsIgnoreCase("FROM")) {
            throw new SqlSyntaxException("Expected FROM");
        }
        queryTokens.remove(0);

        String tableName = queryTokens.remove(0);

        return new commands.DeleteCommand(tableName);
    }

    private SqlCommand parseCreate(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);

        if (!queryTokens.get(0).equalsIgnoreCase("TABLE")) {
            throw new SqlSyntaxException("Expected TABLE");
        }
        queryTokens.remove(0);

        String tableName = queryTokens.remove(0);

        if (!queryTokens.get(0).equals("(")) {
            throw new SqlSyntaxException("Expected '(' after table name");
        }
        queryTokens.remove(0);

        List<String> columns = new ArrayList<>();
        while (!queryTokens.get(0).equals(")")) {
            if (queryTokens.get(0).equals(",")) {
                queryTokens.remove(0);
                continue;
            }
            columns.add(queryTokens.remove(0));
        }
        queryTokens.remove(0);

        return new commands.CreateCommand(tableName, columns);
    }

    private WhereClause parseWhereClause(List<String> queryTokens) throws SqlSyntaxException {
        queryTokens.remove(0);
        String whereColumn = queryTokens.remove(0);
        if (!queryTokens.remove(0).equalsIgnoreCase("=")) {
            throw new SqlSyntaxException("Expected = in WHERE clause");
        }
        String whereValue = queryTokens.remove(0);
        return new WhereClause(whereColumn, whereValue);
    }
}
