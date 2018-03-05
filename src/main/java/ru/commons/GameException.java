package ru.commons;

public class GameException extends Exception {

    private String errorCode;

    public GameException(int code) {
        switch (code) {
            case Commands.ERROR_NULL_BALANCE:
                errorCode = "Your balance is zero";
                break;
            case Commands.ERROR_NOT_ENOUGH_MONEY:
                errorCode = "Not enough money to make bet";
                break;
            default:
                break;
        }
    }


    public String getCode() {
        return errorCode;
    }
}
