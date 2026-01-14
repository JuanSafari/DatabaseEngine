package parser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public Tokenizer() {}

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

            if (c == '\'' || c == '"') {
                int start = ++i;
                while (i < queryLength && query.charAt(i) != c) i++;
                tokens.add(query.substring(start, i));
                i++;
                continue;
            }

            int start = i;
            while (i < queryLength && !Character.isWhitespace(query.charAt(i)) &&
                    query.charAt(i) != '(' && query.charAt(i) != ')' &&
                    query.charAt(i) != ',' && query.charAt(i) != ';' &&
                    query.charAt(i) != '=') {
                i++;
            }
            tokens.add(query.substring(start, i));
        }

        return tokens;
    }
}