package org.example;

import java.util.Scanner;

public class Keyboard {

    private final Scanner keyboard;
    private static Keyboard instance;

    private Keyboard() {
        keyboard = new Scanner(System.in);
    }

    public static Keyboard getInstance() {
        if (instance == null)
            instance = new Keyboard();
        return instance;
    }

    public Scanner getKeyboard() {
        return keyboard;
    }
}
