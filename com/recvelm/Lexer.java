package com.recvelm;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.recvelm.additions.Console;

public class Lexer {

    private String code;
    private int position = 0;
    private int line = 0;
    public ArrayList<Token> tokens = new ArrayList<>();
    private Console console;

    private TokenTypes tokenTypes = new TokenTypes();

    public Lexer(String code) {
        this.code = code;
        this.console = new Console(this.code);
    }

    public ArrayList<Token> lexAnalysis() {
        String[] lines = this.code.split("\n");
        for (String line : lines) {
            this.line += 1;
            while (nextToken(line)) {
            }
            if (this.tokens.size() != 0) {
                this.tokens.add(new Token(this.tokenTypes.get.get("Line"), "\\n", this.line,
                        this.position));
            }
            this.position = 0;
        }
        return this.tokens;
    }

    public boolean nextToken(String line) {
        line = line.replaceAll("// ?.+$", "");
        line = line.replaceAll("(\\r|\\n)$", "");
        if (this.position >= line.length()) {
            return false;
        }
        for (Map.Entry<String, TokenType> pair : this.tokenTypes.get.entrySet()) {
            TokenType tokenType = pair.getValue();
            String regex = "^" + tokenType.regex;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line.substring(this.position));
            boolean result = matcher.find();
            if (result) {
                line = line.substring(this.position);
                String substring = line.substring(matcher.start(), matcher.end());
                this.position += substring.length();
                if (!tokenType.name.equals("space")) {
                    Token token = new Token(tokenType, substring, this.line, this.position);
                    console.log(token.type.name + " : " + token.text + " -> " + position);
                    this.tokens.add(token);
                }
                return true;
            }
        }
        console.error("Unknown token", "SyntaxError", true, this.line,
                this.position);
        return false;
    }

    public boolean action() {
        return false;
    }

}
