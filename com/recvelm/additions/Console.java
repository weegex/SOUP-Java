package com.recvelm.additions;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Console {

    private Map<String, String> style_map = new HashMap<String, String>();
    private String code;

    public Console(String code) {
        this.code = code;
        this.style_map.put("reset_all", "\033[0m");
        this.style_map.put("reset", "\033[39m\033[49m");
        this.style_map.put("bright", "\033[1m");
        this.style_map.put("dim", "\033[2m");
        this.style_map.put("normal", "\033[22m");
        this.style_map.put("black", "\033[30m");
        this.style_map.put("red", "\033[31m");
        this.style_map.put("green", "\033[32m");
        this.style_map.put("yellow", "\033[33m");
        this.style_map.put("blue", "\033[34m");
        this.style_map.put("magenta", "\033[35m");
        this.style_map.put("cyan", "\033[36m");
        this.style_map.put("white", "\033[37m");
        this.style_map.put("grey", "\033[1m\033[30m");
        this.style_map.put("bblack", "\033[40m");
        this.style_map.put("bred", "\033[41m");
        this.style_map.put("bgreen", "\033[42m");
        this.style_map.put("byellow", "\033[43m");
        this.style_map.put("bblue", "\033[44m");
        this.style_map.put("bmagenta", "\033[45m");
        this.style_map.put("bcyan", "\033[46m");
        this.style_map.put("bwhite", "\033[47m");
    }

    public Object print(Object text, String end) {
        System.out.print(text + end);
        return text;
    }

    public Object print(Object text) {
        return print(text, "\n");
    }

    public String getTime() {
        LocalTime currentTime = LocalTime.now();
        int hours = currentTime.getHour() + 1;
        int minutes = currentTime.getMinute() + 1;
        if (minutes < 10) {
            return hours + ":0" + minutes;
        }
        return hours + ":" + minutes;
    }

    public Object log(Object text) {
        return print(style("[" + getTime() + "] ", "bright cyan") + text);
    }

    public Object success(Object text) {
        return print(style("[" + getTime() + "] ", "bright green") + text);
    }

    public void error(Object text, Object errorClass, boolean onCode, int line, int positon) {
        // print(style("[" + getTime() + "] ", "bright red") + text);
        if (onCode) {
            String string = style(
                    "[" + getTime() + "] " + errorClass + " -> " + text + ":\n",
                    "bright red");
            String[] lines = this.code.split("\n");
            string += style(line, "bright grey") + " " + lines[line - 1];
            String spaces = " ".repeat(positon + String.valueOf(line).length() - String.valueOf(positon).length() - 1)
                    + style(positon, "bright grey") + style(" ^", "bright red");
            string += "\n" + spaces;
            print(string);
        } else {
            String string = style(
                    "[" + getTime() + "] [Line: " + line + "; Position: " + positon + "] " + errorClass + " -> " + text,
                    "bright red");
            print(string);
        }
        System.exit(0);
    }

    public void error(String text, String errorClass, boolean onCode) {
        error(text, errorClass, onCode, 0, 0);
    }

    public String style(Object text, String style) {
        String[] style_list = style.split(" ");
        for (String s : style_list) {
            String getted_style = style_map.get(s);
            if (getted_style != null) {
                text = getted_style + text;
            }
        }
        return text + style_map.get("reset_all");
    }

}
