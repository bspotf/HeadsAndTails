package ru.server;

import ru.commons.Commands;
import ru.commons.GameException;

import java.util.ArrayList;

public class Game {
    private Player player;

    private static ArrayList<String> history;
    private int playedGames = 0;
    private boolean boolCoinSide;
    private String side;

    public Game(int coins) {
        player = new Player(coins);
        history = new ArrayList<>();

    }

    public Game() {
        this(100);
    }

    public int[] playGame(int bet, int coinSide) throws GameException {

        switch (coinSide) {
            case Commands.HEAD:
                boolCoinSide = true;
                side = "Head";
                break;
            case Commands.TAIL:
                boolCoinSide = false;
                side = "Tail";
                break;
        }
        if (bet <= player.getCoins()) {
            boolean cast = (Math.random() < 0.5);
            int result;
            String returnString;
            if (cast == boolCoinSide) {
                int gain = (int) Math.ceil(bet * 0.9);
                player.addCoins(gain);
                returnString = "Game result: " + gain + " | Bet on " +
                        side + " | Balance: " + player.getCoins();
                addToHistory(returnString);
                result = Commands.WIN;
            } else {
                player.addCoins(-bet);
                returnString = "Game result: " + (-bet) + " | Bet on " +
                        side + " | Balance: " + player.getCoins();
                addToHistory(returnString);
                result = Commands.LOSE;
            }
            return new int[]{result, player.getCoins()};
        } else {
            if (player.getCoins() == 0) {
            throw new GameException(Commands.ERROR_NULL_BALANCE);
            }
            throw new GameException(Commands.ERROR_NOT_ENOUGH_MONEY);
        }
    }

    public String getGameHistory() {
        StringBuilder historyString = new StringBuilder();
        if (history.size() != -1) {
            for (String bet : history) {
                historyString.append(bet).append("\n");
            }
        } else {
            historyString = new StringBuilder("No results yet");
        }
        return historyString.toString();
    }

    private void addToHistory(String stats) {
        history.add(playedGames, String.valueOf(stats));
        incrementPlayedGames();
    }

    public ArrayList<String> getHistoryArray() {
        return history;
    }

    private void incrementPlayedGames() {
        playedGames++;
    }

}
