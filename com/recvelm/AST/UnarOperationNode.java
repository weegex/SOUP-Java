package com.recvelm.AST;

import com.recvelm.Token;

public class UnarOperationNode extends Node {

    public Token operator;
    public Node operand;

    public UnarOperationNode(Token operator, Node operand) {
        this.operator = operator;
        this.operand = operand;
    }

}
