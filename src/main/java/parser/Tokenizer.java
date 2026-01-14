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

            if (startsWithIgnoreCase(query, i)) {
                tokens.add(query.substring(i, i + 6));
                i += 6;

                while (i < queryLength && Character.isWhitespace(query.charAt(i))) i++;
                if (i < queryLength && query.charAt(i) == '(') {
                    tokens.add("(");
                    i++;

                    StringBuilder valueBuilder = new StringBuilder();
                    while (i < queryLength) {
                        while (i < queryLength && Character.isWhitespace(query.charAt(i))) i++;
                        if (i < queryLength && query.charAt(i) == ')') {
                            tokens.add(valueBuilder.toString());
                            valueBuilder.setLength(0);
                            tokens.add(")");
                            i++;
                            break;
                        }

                        if (i < queryLength && query.charAt(i) == ',') {
                            tokens.add(valueBuilder.toString());
                            valueBuilder.setLength(0);
                            tokens.add(",");
                            i++;
                            continue;
                        }

                        if (i < queryLength && (query.charAt(i) == '\'' || query.charAt(i) == '"')) {
                            char quoteChar = query.charAt(i);
                            int start = i + 1;
                            i++;
                            while (i < queryLength && query.charAt(i) != quoteChar) i++;
                            String val = query.substring(start, i);
                            tokens.add(val);
                            i++;

                            continue;
                        }

                        while (i < queryLength && query.charAt(i) != ',' && query.charAt(i) != ')') {
                            valueBuilder.append(query.charAt(i));
                            i++;
                        }

                        String val = valueBuilder.toString().trim();
                        if (!val.isEmpty()) {
                            tokens.add(val);
                        }
                        valueBuilder.setLength(0);
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
        return tokens;
    }

    private boolean startsWithIgnoreCase(String s, int offset) {
        int n = "values".length();
        if (offset + n > s.length()) return false;
        return s.regionMatches(true, offset, "values", 0, n);
    }
}
