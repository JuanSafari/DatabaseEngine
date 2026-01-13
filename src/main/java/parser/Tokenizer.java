package parser;

import java.util.List;

public class Tokenizer {
    public Tokenizer() {
    }

    public List<String> tokenize(String query) {
        query = query.replaceAll("([(),])", " $1 ");
        query = query.trim().replaceAll("\\s+", " ");
        return List.of(query.split(" "));
    }
}
