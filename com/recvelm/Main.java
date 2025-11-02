package com.recvelm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import com.recvelm.AST.Node;
import com.recvelm.additions.Console;

public class Main {

    private static String code;
    private static Scanner scan = new Scanner(System.in);
    private static Node root;
    private static Console console = new Console("");

    public static void main(String[] args) {
        String input = scan.nextLine();
        // String[] input_list = input.split("/");
        // String file_name = input_list[-1];
        Path path = Path.of(input);
        try {
            code = Files.readString(path);
        } catch (IOException e) {
            console.error("Error opening file", "FileError", false);
        }
        Lexer lexer = new Lexer(code);
        ArrayList<Token> tokens = lexer.lexAnalysis();
        // for (Token token : tokens) {
        // System.out.println(token.type.name + ":" + token.text + " " + token.line +
        // ":" + token.position);
        // }
        Parser parser = new Parser(tokens, code);
        root = parser.parseCode();
        parser.run(root);
    }

}
