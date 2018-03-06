package ru.server;

import ru.commons.Commands;
import ru.commons.GameException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadClientHandler implements Runnable {

    private Socket threadSocket;

    public ThreadClientHandler(Socket client) throws IOException {
        this.threadSocket = client;
    }

    @Override
    public void run() {
        //Scheduler
        try (DataInputStream is = new DataInputStream(threadSocket.getInputStream());
             DataOutputStream os = new DataOutputStream(threadSocket.getOutputStream())
        ) {
            // Initialize the Game
            Game game = new Game();
            System.out.println("Game initialized");
            // Begin dialogue with server while it is open
            if (!threadSocket.isClosed()) {
                os.write(Commands.HELLO_MESSAGE);
            }
            LOOP:
            while (!threadSocket.isClosed()) {
                int entryData = is.read();
//                String entryData = is.readUTF();
                switch (entryData) {
                    case Commands.PLAY:
                        int coinSide;
                        do {
                            os.write(Commands.SIDE);
                            coinSide = is.read();
                        } while (coinSide != Commands.HEAD &&
                                coinSide != Commands.TAIL);
                        int bet;
                        do {
                            os.write(Commands.BET);
                            bet = Integer.parseInt(is.readUTF());
                        } while (bet <= 0);
                        try {
                            int[] res = game.playGame(bet, coinSide);
                            os.write(Commands.RESULT);
                            for (int m : res) {
                                os.write(m);
                            }
                            break;
                        } catch (GameException e) {
                            int exCode = e.getCode();
                            os.write(exCode);
                            if (exCode == Commands.ERROR_NULL_BALANCE) {
                                os.write(Commands.CLOSING_CONNECTION);
                                break LOOP;
                            }
                        }
                    case Commands.HISTORY:
                        os.write(Commands.HISTORY);
                        os.writeUTF(game.getGameHistory());
                        break;
                    case Commands.QUIT:
                        System.out.println("Closing connection");
                        os.write(Commands.CLOSING_CONNECTION);
                        disconnect();
                        os.flush();
                        break LOOP;
                    default:
                        os.write(Commands.ERROR_WRONG_COMMAND);
                        break;
                }
                os.flush();
            }

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
}
