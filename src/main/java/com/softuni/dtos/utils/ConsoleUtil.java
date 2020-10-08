package com.softuni.dtos.utils;

import org.springframework.stereotype.Component;

@Component
public class ConsoleUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void printInRed(String message){
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    public static void printInGreen(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    public static void printInYellow(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }
}
