package parser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public Tokenizer() {
    }

    public List<String> tokenize(String query) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        int queryLength = query.length();
        while (i < queryLength) {
            char c = query.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '(' || c == ')' || c == ',' || c == ';' || c == '=') {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            if (startsWithValuesIgnoreCase(query, i)) {
                tokens.add(query.substring(i, i + 6));
                i += 6;

                while (i < queryLength && Character.isWhitespace(query.charAt(i))) i++;
                if (i < queryLength && query.charAt(i) == '(') {
                    tokens.add("(");
                    i++;

                    while (i < queryLength) {
                        while (i < queryLength && Character.isWhitespace(query.charAt(i))) i++;
                        if (i >= queryLength) break;

                        char current = query.charAt(i);

                        if (current == ')') {
                            tokens.add(")");
                            i++;
                            break;
                        }
                        if (current == ',') {
                            tokens.add(",");
                            i++;
                            continue;
                        }

                        if (current == '\'' || current == '"') {
                            int start = ++i;
                            while (i < queryLength && query.charAt(i) != current) i++;
                            tokens.add(query.substring(start, i));
                            i++;
                            continue;
                        }

                        int start = i;
                        while (i < queryLength && query.charAt(i) != ',' && query.charAt(i) != ')' && !Character.isWhitespace(query.charAt(i))) {
                            i++;
                        }
                        String val = query.substring(start, i).trim();
                        if (!val.isEmpty()) {
                            tokens.add(val);
                        }
                    }
                    continue;
                }
                continue;
            }

            if (c == '\'' || c == '"') {
                int start = i + 1;
                i++;
                while (i < queryLength && query.charAt(i) != c) i++;
                tokens.add(query.substring(start, i));
                i++;
                continue;
            }

            int start = i;
            while (i < queryLength && !Character.isWhitespace(query.charAt(i)) &&
                    query.charAt(i) != '(' && query.charAt(i) != ')' &&
                    query.charAt(i) != ',' && query.charAt(i) != ';') {
                i++;
            }
            tokens.add(query.substring(start, i));
        }

        for (String token : tokens) {
            System.out.println(token);
        }

        return tokens;
    }

    private boolean startsWithValuesIgnoreCase(String s, int offset) {
        int n = "values".length();
        if (offset + n > s.length()) return false;
        return s.regionMatches(true, offset, "values", 0, n);
    }
}
