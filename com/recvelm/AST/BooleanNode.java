package com.recvelm.AST;

import com.recvelm.Token;

public class BooleanNode extends Node {

    public Token token;

    public BooleanNode(Token token) {
        this.token = token;
    }

}
