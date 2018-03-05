package ru.server;

import ru.commons.Commands;
import ru.commons.GameException;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Player player;
//    private HistoryService history;

    private static ArrayList<String> history;
    private int playedGames = 0;
    private boolean boolCoinSide;
    private String returnString;
    private final Random random;
    private String side;

    private double multiplier = 1.9;


    public Game(int coins) {
        // Initialize player
        player = new Player(coins);
        history = new ArrayList<String>();
        random = new Random();
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
//            boolean cast = (random.nextInt(10) > 5);
            int result;
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
//                return "Your balance is zero.\nThe end.";
            throw new GameException(Commands.ERROR_NULL_BALANCE);
            }
//            return new int[]{Commands.ERROR_NOT_ENOUGH_MONEY, player.getCoins()};
            throw new GameException(Commands.ERROR_NOT_ENOUGH_MONEY);
        }
    }

    public String getHistory() {
        String historyString = "";
        if (history.size() != -1) {
            for (String bet : history) {
                historyString += bet + "\n";
            }
        } else historyString = "No results yet";
        return historyString;
    }

    private void addToHistory(String stats) {
        history.add(playedGames, String.valueOf(stats));
        incrementPlayedGames();
    }

    private void incrementPlayedGames() {
        playedGames++;
    }

    public int getPlayedGames() {
        return playedGames;
    }

}
