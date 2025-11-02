package com.recvelm;

import java.util.Map;
import java.util.TreeMap;

public class TokenTypes {

    public Map<String, TokenType> get = new TreeMap<String, TokenType>();

    public TokenTypes() {
        this.get.put("Integer", new TokenType("int", "[0-9]+"));
        this.get.put("Float", new TokenType("float", "[0-9]+\\.[0-9]+"));
        this.get.put("String", new TokenType("str", "\".*\"|\'.*\'"));
        this.get.put("Constant", new TokenType("const", "const"));
        this.get.put("Boolean", new TokenType("bool", "true|false"));
        this.get.put("None", new TokenType("none", "none"));
        this.get.put("Method", new TokenType("method", "method"));
        this.get.put("Space", new TokenType("space", "\s+"));
        this.get.put("Line", new TokenType("Line", "\n"));
        this.get.put("Assign", new TokenType("=", "="));
        this.get.put("Plus", new TokenType("+", "\\+"));
        this.get.put("Minus", new TokenType("-", "-"));
        this.get.put("Multiply", new TokenType("*", "\\*"));
        this.get.put("Divide", new TokenType("/", "/"));
        this.get.put("zQuote", new TokenType("'", "'"));
        this.get.put("zDoubleQuote", new TokenType("\"", "\""));
        this.get.put("Comma", new TokenType(",", ","));
        this.get.put("zDot", new TokenType(".", "\\."));
        this.get.put("ParenthesisLeft", new TokenType("(", "\\("));
        this.get.put("ParenthesisRight", new TokenType(")", "\\)"));
        this.get.put("BraceLeft", new TokenType("{", "\\{"));
        this.get.put("BraceRighr", new TokenType("}", "\\}"));
        this.get.put("BrakedLeft", new TokenType("[", "\\["));
        this.get.put("BrakedRight", new TokenType("]", "\\]"));
        this.get.put("zVariable", new TokenType("var", "[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9]*"));
    }

}
