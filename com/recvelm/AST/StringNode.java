package com.recvelm.AST;

import com.recvelm.Token;

public class StringNode extends Node {

    public Token token;

    public StringNode(Token token) {
        this.token = token;
    }

}
