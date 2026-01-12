package parser;

import java.util.List;

public class Tokenizer {
    public Tokenizer() {
    }

    public List<String> tokenize(String query) {
        return List.of((query.split("[ ,]+")));
    }
}
