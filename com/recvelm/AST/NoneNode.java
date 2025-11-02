package com.recvelm.AST;

import com.recvelm.Token;

public class NoneNode extends Node {

    public Token token;

    public NoneNode(Token token) {
        this.token = token;
    }

}
