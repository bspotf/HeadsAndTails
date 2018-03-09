package ru.server;

import ru.commons.Commands;
import ru.commons.GameException;
import ru.commons.GameState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientHandler implements Runnable {

    private Socket threadSocket;
    private GameState gameState;
    private int coinSide;
    private int bet;
    private HistoryService historyService;
    private int sessionId;

    public ServerClientHandler(Socket client, int id, HistoryService historyService) throws IOException {
        this.threadSocket = client;
        this.sessionId = id;
        this.historyService = historyService;
    }

    @Override
    public void run() {
        //Scheduler
        try (DataInputStream is = new DataInputStream(threadSocket.getInputStream());
             DataOutputStream os = new DataOutputStream(threadSocket.getOutputStream())
        ) {
            // Initialize the Game
            Game game = new Game();
            historyService.add(sessionId, game.getHistoryArray());
            System.out.println("Game initialized");
            // Begin dialogue with server while it is open
            if (!threadSocket.isClosed()) {
                os.write(Commands.HELLO_MESSAGE);
                gameState = GameState.IS_READY_TO_PLAY;
            }
            while (!threadSocket.isClosed()) {
                int entryData = is.read();
                switch (entryData) {
                    case Commands.PLAY:
                        if (gameState == GameState.IS_READY_TO_PLAY) {
                            os.write(Commands.SIDE);
                            gameState = GameState.IS_WAITING_FOR_SIDE;
                        } else {
                            os.write(Commands.ERROR_WRONG_COMMAND);
                        }
                        break;
                    case Commands.HEAD:
                    case Commands.TAIL:
                        if (gameState == GameState.IS_WAITING_FOR_SIDE) {
                            coinSide = entryData;
                            gameState = GameState.IS_WAITING_FOR_BET;
                            os.write(Commands.BET);
                        } else {
                            os.write(Commands.ERROR_WRONG_COMMAND);
                        }
                        break;
                    case Commands.BET:
                        if (gameState == GameState.IS_WAITING_FOR_BET) {
                            bet = Integer.parseInt(is.readUTF());
                            if (bet <= 0) {
                                os.write(Commands.BET);
                            } else {
                                try {
                                    int[] res = game.playGame(bet, coinSide);
                                    os.write(Commands.RESULT);
                                    for (int m : res) {
                                        os.write(m);
                                    }
                                    os.write(Commands.AGAIN);
                                    gameState = GameState.IS_READY_TO_PLAY;
                                    break;
                                } catch (GameException e) {
                                    int exCode = e.getCode();
                                    os.write(exCode);
                                    if (exCode == Commands.ERROR_NULL_BALANCE) {
                                        os.write(Commands.CLOSING_CONNECTION);
                                        disconnect();
                                    }
                                }
                            }
                        } else {
                            os.write(Commands.ERROR_WRONG_COMMAND);
                        }
                        break;
                    case Commands.HISTORY:
                        if (gameState == GameState.IS_READY_TO_PLAY) {
                            os.write(Commands.HISTORY);
                            os.writeUTF(game.getGameHistory());
                        } else {
                            os.write(Commands.ERROR_WRONG_COMMAND);
                        }
                        break;
                    case Commands.QUIT:
                        System.out.println("Closing connection");
                        os.write(Commands.CLOSING_CONNECTION);
                        disconnect();
                        os.flush();
                    default:
                        os.write(Commands.ERROR_WRONG_COMMAND);
                        break;
                }
                os.flush();
            }
            os.write(Commands.CLOSING_CONNECTION);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            disconnect();
        }

    }

    private void disconnect() {
        try {
            System.out.println("Closing socket");
            threadSocket.close();
        } catch (IOException e) {
            System.err.println("Socket closing error");
//            e.printStackTrace();
        }
        System.out.println(" DONE");
    }

    private void betAndPlay() {

    }
}
