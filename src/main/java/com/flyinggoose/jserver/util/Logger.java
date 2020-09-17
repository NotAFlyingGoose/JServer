package com.flyinggoose.jserver.util;

public class Logger {
    public static void log(String author, Object text) {
        System.out.println("[" + author + "] : " + text);
    }
}
