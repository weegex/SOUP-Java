package com.recvelm.AST;

import com.recvelm.Token;

public class VariableNode extends Node {

    public Token token;

    public VariableNode(Token token) {
        this.token = token;
    }

}
