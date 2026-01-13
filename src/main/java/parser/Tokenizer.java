package parser;

import java.util.List;

public class Tokenizer {
    public Tokenizer() {
    }

    public List<String> tokenize(String query) {
        List<String> tokens = new java.util.ArrayList<>();
        int i = 0;
        int n = query.length();
        while (i < n) {
            char c = query.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '(' || c == ')' || c == ',' || c == ';') {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            if (startsWithIgnoreCase(query, i, "values")) {
                tokens.add(query.substring(i, i + 6));
                i += 6;

                while (i < n && Character.isWhitespace(query.charAt(i))) i++;
                if (i < n && query.charAt(i) == '(') {
                    tokens.add("(");
                    i++;

                    StringBuilder valueBuilder = new StringBuilder();
                    boolean insideValue = false;
                    while (i < n) {
                        while (i < n && Character.isWhitespace(query.charAt(i))) i++;
                        if (i < n && query.charAt(i) == ')') {
                            if (insideValue) {
                                tokens.add(valueBuilder.toString());
                                valueBuilder.setLength(0);
                                insideValue = false;
                            }
                            tokens.add(")");
                            i++;
                            break;
                        }

                        if (i < n && query.charAt(i) == ',') {
                            if (insideValue) {
                                tokens.add(valueBuilder.toString());
                                valueBuilder.setLength(0);
                                insideValue = false;
                            }
                            tokens.add(",");
                            i++;
                            continue;
                        }

                        if (i < n && (query.charAt(i) == '\'' || query.charAt(i) == '"')) {
                            char quoteChar = query.charAt(i);
                            int start = i + 1;
                            i++;
                            while (i < n && query.charAt(i) != quoteChar) i++;
                            String val = query.substring(start, i);
                            tokens.add(val);
                            i++;

                            insideValue = false;
                            continue;
                        }

                        int start = i;
                        while (i < n && query.charAt(i) != ',' && query.charAt(i) != ')' ) {
                            valueBuilder.append(query.charAt(i));
                            i++;
                        }

                        String val = valueBuilder.toString().trim();
                        if (!val.isEmpty()) {
                            tokens.add(val);
                        }
                        valueBuilder.setLength(0);
                        insideValue = false;
                    }
                    continue;
                }
                continue;
            }

            if (c == '\'' || c == '"') {
                char quoteChar = c;
                int start = i + 1;
                i++;
                while (i < n && query.charAt(i) != quoteChar) i++;
                tokens.add(query.substring(start, i));
                i++;
                continue;
            }

            int start = i;
            while (i < n && !Character.isWhitespace(query.charAt(i)) &&
                    query.charAt(i) != '(' && query.charAt(i) != ')' &&
                    query.charAt(i) != ',' && query.charAt(i) != ';') {
                i++;
            }
            tokens.add(query.substring(start, i));
        }
        return tokens;
    }

    private boolean startsWithIgnoreCase(String s, int offset, String prefix) {
        int n = prefix.length();
        if (offset + n > s.length()) return false;
        return s.regionMatches(true, offset, prefix, 0, n);
    }
}
