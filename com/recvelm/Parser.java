package com.recvelm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.recvelm.AST.*;
import com.recvelm.additions.Console;

public class Parser {

    private ArrayList<Token> tokens;
    private String code;
    private int position = 0;
    public Map<String, Object> scope = new HashMap<String, Object>();
    public Map<String, Object> lockedScope = new HashMap<String, Object>();
    private TokenTypes tokenTypes = new TokenTypes();
    private Console console;

    public Parser(ArrayList<Token> tokens, String code) {
        this.tokens = tokens;
        this.code = code;
        this.console = new Console(this.code);
    }

    private Token getPrev() {
        if (this.position < this.tokens.size()) {
            if (this.position - 1 >= 0) {
                Token prevToken = this.tokens.get(this.position - 1);
                if (prevToken != null) {
                    return prevToken;
                }
            }
        }
        return null;
    }

    private Token match(String... expectedes) {
        if (this.position < this.tokens.size()) {
            Token current = this.tokens.get(this.position);
            for (String expected : expectedes) {
                if (this.tokenTypes.get.get(expected).name == current.type.name) {
                    this.position += 1;
                    return current;
                }
            }
        }
        return null;
    }

    private Token require(String expected) {
        Token token = match(expected);
        if (token == null) {
            Token prevToken = getPrev();
            console.error("Expected " + this.tokenTypes.get.get(expected).name, "NotFoundError", true,
                    prevToken.line,
                    prevToken.position + 1);
        }
        return token;
    }

    public StatementsNode parseCode() {
        StatementsNode root = new StatementsNode();
        while (this.position < this.tokens.size()) {
            Node codeStringNode = parseException();
            root.addNode(codeStringNode);
        }
        return root;
    }

    private Node parseException() {
        Token constant = match("Constant");
        if (constant != null) {
            removeSpaces();
            require("zVariable");
            this.position -= 1;
            Node variableNode = parseDataType();
            removeSpaces();
            Token assignOperator = match("Assign");
            if (assignOperator != null) {
                Node rightFormulaNode = parseFormula();
                if (rightFormulaNode != null) {
                    BinOperationNode binaryNode = new BinOperationNode(constant, variableNode, rightFormulaNode);
                    return binaryNode;
                }
                console.error("Assignment data is empty", "SyntaxError", true, assignOperator.line,
                        assignOperator.position);
            } else {
                UnarOperationNode unaryNode = new UnarOperationNode(constant, variableNode);
                return unaryNode;
            }
        }
        Token variable = match("zVariable");
        if (variable != null) {
            this.position -= 1;
            Node variableNode = parseDataType();
            removeSpaces();
            Token assignOperator = match("Assign");
            if (assignOperator != null) {
                Node rightFormulaNode = parseFormula();
                if (rightFormulaNode != null) {
                    BinOperationNode binaryNode = new BinOperationNode(
                            assignOperator, variableNode, rightFormulaNode);
                    return binaryNode;
                }
                console.error("Assignment data is empty", "SyntaxError", true, assignOperator.line,
                        assignOperator.position);
            }
            return new VariableNode(variable);
        }
        return parseDataType();
    }

    private void removeSpaces() {
        while (match("Space") != null) {

        }
    }

    private Node parseDefault() {
        this.position += 1;
        return null;
    }

    private Node parseDataType() {
        Token integer = match("Integer");
        if (integer != null) {
            return new IntegerNode(integer);
        }
        Token fl = match("Float");
        if (fl != null) {
            return new FloatNode(fl);
        }
        Token variable = match("zVariable");
        if (variable != null) {
            return new VariableNode(variable);
        }
        Token string = match("String");
        if (string != null) {
            return new StringNode(string);
        }
        Token quote = match("zQuote");
        if (quote != null) {
            require("zQuote");
        }
        Token doubleQuote = match("zDoubleQuote");
        if (doubleQuote != null) {
            require("zDoubleQuote");
        }
        Token none = match("None");
        if (none != null) {
            return new NoneNode(none);
        }
        Token bool = match("Boolean");
        if (bool != null) {
            return new BooleanNode(bool);
        }
        return parseDefault();
    }

    private Node parseFormula() {
        Node leftNode = parseParentheses();
        removeSpaces();
        Token operator = match("Plus", "Minus", "Multiply", "Divide");
        while (operator != null) {
            Node rightNode = parseParentheses();
            leftNode = new BinOperationNode(operator, leftNode, rightNode);
            removeSpaces();
            operator = match("Plus", "Minus", "Multiply", "Divide");
        }
        return leftNode;
    }

    private Node parseParentheses() {
        removeSpaces();
        if (match("ParenthesisLeft") != null) {
            Node node = parseFormula();
            removeSpaces();
            require("ParenthesisRight");
            return node;
        } else {
            return parseException();
        }
    }

    private String getClass(Object object) {
        if (object instanceof String) {
            return "str";
        }
        if (object instanceof Integer) {
            return "int";
        }
        if (object instanceof Boolean) {
            return "bool";
        }
        return "none";
    }

    public Object run(Node node) {

        if (node instanceof IntegerNode) {
            IntegerNode _node = (IntegerNode) node;
            return Integer.parseInt(_node.token.text);
        }

        if (node instanceof FloatNode) {
            FloatNode _node = (FloatNode) node;
            return Float.parseFloat(_node.token.text);
        }

        if (node instanceof VariableNode) {
            VariableNode _node = (VariableNode) node;
            Object c_result = this.lockedScope.get(_node.token.text);
            if (c_result == null) {
                Object result = this.scope.get(_node.token.text);
                if (result != null) {
                    return result;
                }
            } else {
                return c_result;
            }
            console.error("Variable not assigned", "AssignError", true, _node.token.line, _node.token.position);
        }

        if (node instanceof StringNode) {
            StringNode _node = (StringNode) node;
            String text = _node.token.text;
            return text.substring(1, text.length() - 1);
        }

        if (node instanceof NoneNode) {
            return "none";
        }

        if (node instanceof BooleanNode) {
            BooleanNode _node = (BooleanNode) node;
            if (_node.token.text.equals("true")) {
                return true;
            } else {
                return false;
            }
        }

        if (node instanceof BinOperationNode) {
            BinOperationNode _node = (BinOperationNode) node;
            switch (_node.operator.type.name) {
                case "+":
                    Object leftNode_ = run(_node.leftNode);
                    Object rightNode_ = run(_node.rightNode);
                    if (leftNode_ instanceof String || rightNode_ instanceof String) {
                        return leftNode_.toString() + rightNode_.toString();
                    }
                    if (leftNode_ instanceof Integer && rightNode_ instanceof Integer) {
                        int leftNode = (Integer) leftNode_;
                        int rightNode = (Integer) rightNode_;
                        return leftNode + rightNode;
                    }
                    if (leftNode_ instanceof Float || rightNode_ instanceof Float) {
                        if (leftNode_ instanceof Integer && rightNode_ instanceof Float) {
                            int _leftNode = (Integer) leftNode_;
                            float leftNode = Float.valueOf(_leftNode);
                            float rightNode = (Float) rightNode_;
                            return leftNode + rightNode;
                        } else if (leftNode_ instanceof Float && rightNode_ instanceof Integer) {
                            int _rightNode = (Integer) rightNode_;
                            float rightNode = Float.valueOf(_rightNode);
                            float leftNode = (Float) leftNode_;
                            return leftNode + rightNode;
                        } else {
                            float leftNode = (Float) leftNode_;
                            float rightNode = (Float) rightNode_;
                            return leftNode + rightNode;
                        }
                    }
                    console.error(
                            "Cannot calculate \"" + getClass(leftNode_) + "\" and \"" + getClass(rightNode_) + "\"",
                            "CalculateError", true, _node.operator.line, _node.operator.position);
                case "-":
                    Object leftNode_1 = run(_node.leftNode);
                    Object rightNode_1 = run(_node.rightNode);
                    if (leftNode_1 instanceof Integer && rightNode_1 instanceof Integer) {
                        int leftNode = (Integer) leftNode_1;
                        int rightNode = (Integer) rightNode_1;
                        return leftNode - rightNode;
                    }
                    if (leftNode_1 instanceof Float || rightNode_1 instanceof Float) {
                        if (leftNode_1 instanceof Integer && rightNode_1 instanceof Float) {
                            int _leftNode = (Integer) leftNode_1;
                            float leftNode = Float.valueOf(_leftNode);
                            float rightNode = (Float) rightNode_1;
                            return leftNode - rightNode;
                        } else if (leftNode_1 instanceof Float && rightNode_1 instanceof Integer) {
                            int _rightNode = (Integer) rightNode_1;
                            float rightNode = Float.valueOf(_rightNode);
                            float leftNode = (Float) leftNode_1;
                            return leftNode - rightNode;
                        } else {
                            float leftNode = (Float) leftNode_1;
                            float rightNode = (Float) rightNode_1;
                            return leftNode - rightNode;
                        }
                    }
                    console.error(
                            "Cannot calculate \"" + getClass(leftNode_1) + "\" and \"" + getClass(rightNode_1) + "\"",
                            "CalculateError", true,
                            _node.operator.line, _node.operator.position);
                case "*":
                    Object leftNode_2 = run(_node.leftNode);
                    Object rightNode_2 = run(_node.rightNode);
                    if (leftNode_2 instanceof String && rightNode_2 instanceof Integer) {
                        int rightNode = (Integer) rightNode_2;
                        String leftNode = (String) leftNode_2;
                        return leftNode.repeat(rightNode);
                    }
                    if (leftNode_2 instanceof Integer && rightNode_2 instanceof Integer) {
                        int leftNode = (Integer) leftNode_2;
                        int rightNode = (Integer) rightNode_2;
                        return leftNode * rightNode;
                    }
                    if (leftNode_2 instanceof Float || rightNode_2 instanceof Float) {
                        if (leftNode_2 instanceof Integer && rightNode_2 instanceof Float) {
                            int _leftNode = (Integer) leftNode_2;
                            float leftNode = Float.valueOf(_leftNode);
                            float rightNode = (Float) rightNode_2;
                            return leftNode * rightNode;
                        } else if (leftNode_2 instanceof Float && rightNode_2 instanceof Integer) {
                            int _rightNode = (Integer) rightNode_2;
                            float rightNode = Float.valueOf(_rightNode);
                            float leftNode = (Float) leftNode_2;
                            return leftNode * rightNode;
                        } else {
                            float leftNode = (Float) leftNode_2;
                            float rightNode = (Float) rightNode_2;
                            return leftNode * rightNode;
                        }
                    }
                    console.error(
                            "Cannot calculate \"" + getClass(leftNode_2) + "\" and \"" + getClass(rightNode_2) + "\"",
                            "CalculateError", true,
                            _node.operator.line, _node.operator.position);
                case "/":
                    Object leftNode_3 = run(_node.leftNode);
                    Object rightNode_3 = run(_node.rightNode);
                    if (leftNode_3 instanceof Integer && rightNode_3 instanceof Integer) {
                        int leftNode = (Integer) leftNode_3;
                        int rightNode = (Integer) rightNode_3;
                        return leftNode / rightNode;
                    }
                    if (leftNode_3 instanceof Float || rightNode_3 instanceof Float) {
                        if (leftNode_3 instanceof Integer && rightNode_3 instanceof Float) {
                            int _leftNode = (Integer) leftNode_3;
                            float leftNode = Float.valueOf(_leftNode);
                            float rightNode = (Float) rightNode_3;
                            return leftNode / rightNode;
                        } else if (leftNode_3 instanceof Float && rightNode_3 instanceof Integer) {
                            int _rightNode = (Integer) rightNode_3;
                            float rightNode = Float.valueOf(_rightNode);
                            float leftNode = (Float) leftNode_3;
                            return leftNode / rightNode;
                        } else {
                            float leftNode = (Float) leftNode_3;
                            float rightNode = (Float) rightNode_3;
                            return leftNode / rightNode;
                        }
                    }
                    console.error(
                            "Cannot calculate \"" + getClass(leftNode_3) + "\" and \"" + getClass(rightNode_3) + "\"",
                            "CalculateError", true,
                            _node.operator.line, _node.operator.position);
                case "=":
                    VariableNode variableNode = (VariableNode) _node.leftNode;
                    if (this.lockedScope.get(variableNode.token.text) == null) {
                        Object result = run(_node.rightNode);
                        if (this.lockedScope.get(variableNode.token.text) == null) {
                            this.scope.put(variableNode.token.text, result);
                            return result;
                        }
                    }
                    console.error("Cannot assign variable, variable is constant", "AssignError", true,
                            _node.operator.line, _node.operator.position);
                case "const":
                    VariableNode variableNode1 = (VariableNode) _node.leftNode;
                    if (this.lockedScope.get(variableNode1.token.text) == null) {
                        Object result = run(_node.rightNode);
                        this.scope.remove(variableNode1.token.text);
                        this.lockedScope.put(variableNode1.token.text, result);
                        return result;
                    }
                    console.error("Costant assigned", "AssignError", true, _node.operator.line,
                            _node.operator.position);
            }
        }

        if (node instanceof UnarOperationNode) {
            UnarOperationNode _node = (UnarOperationNode) node;
            switch (_node.operator.type.name) {
                case "const":
                    VariableNode variableNode = (VariableNode) _node.operand;
                    if (this.lockedScope.get(variableNode.token.text) == null) {
                        Object value = this.scope.get(variableNode.token.text);
                        this.lockedScope.put(variableNode.token.text, value);
                        this.scope.remove(variableNode.token.text);
                        return value;
                    }
                    return this.lockedScope.get(variableNode.token.text);
            }
        }

        if (node instanceof StatementsNode) {
            StatementsNode _node = (StatementsNode) node;
            for (Node codeString : _node.codeStrings) {
                run(codeString);
                // console.log(run(codeString) + " : " + codeString);
            }
            console.print("Scope: " + this.scope.toString());
            console.print("Locked scope: " + this.lockedScope.toString());
            return null;
        }
        return null;
    }

}
