package com.recvelm;

public class Token {

    public TokenType type;
    public String text;
    public int line;
    public int position;

    public Token(TokenType type, String text, int line, int position) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.position = position;
    }

}
