package ru.server;

public class Player {
    private int coins = 100;

    public Player(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

}