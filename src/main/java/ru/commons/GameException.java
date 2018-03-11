package ru.commons;

public class GameException extends Exception {

    private int errorCode;

    public GameException(int code) {
        switch (code) {
            case Commands.ERROR_NULL_BALANCE:
                errorCode = Commands.ERROR_NULL_BALANCE;
                break;
            case Commands.ERROR_NOT_ENOUGH_MONEY:
                errorCode = Commands.ERROR_NOT_ENOUGH_MONEY;
                break;
            default:
                break;
        }
    }

    public int getCode() {
        return errorCode;
    }
}
