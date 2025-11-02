package com.recvelm.AST;

import java.util.ArrayList;

public class StatementsNode extends Node {

    public ArrayList<Node> codeStrings = new ArrayList<>();

    public void addNode(Node node) {
        this.codeStrings.add(node);
    }

}
