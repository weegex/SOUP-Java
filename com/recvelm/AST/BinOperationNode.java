package com.recvelm.AST;

import com.recvelm.Token;

public class BinOperationNode extends Node {

    public Token operator;
    public Node leftNode;
    public Node rightNode;

    public BinOperationNode(Token operator, Node leftNode, Node rightNode) {
        this.operator = operator;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

}
