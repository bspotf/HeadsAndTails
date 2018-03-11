package ru.commons;

public class Commands {
    // Dialog commands
    public static final int HELLO_MESSAGE = 1;
    public static final int OK = 2;
    public static final int PING = 3;
    public static final int PONG = 4;
    public static final int HISTORY = 5;
    public static final int RESULT = 6;
    // Exit command
    public static final int CLOSING_CONNECTION = 8;
    public static final int QUIT = 9;
    // Game commands
    public static final int PLAY = 10;
    public static final int BET = 11;
    public static final int SIDE = 12;
    public static final int HEAD = 13;
    public static final int TAIL = 14;
    // Result commands
    public static final int WIN = 11;
    public static final int LOSE = 12;
    public static final int AGAIN = 13;
    // Errors
    public static final int ERROR_WRONG_COMMAND = 100;
    public static final int ERROR_NULL_BALANCE = 101;
    public static final int ERROR_NOT_ENOUGH_MONEY = 102;
}
