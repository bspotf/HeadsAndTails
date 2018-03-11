package ru.client;

import ru.commons.Commands;

import java.io.DataInputStream;
import java.io.IOException;


public class ClientThreadListener extends Thread {
    private DataInputStream is;
    private ClientThread client;

    public ClientThreadListener(ClientThread client) throws IOException {
        this.client = client;
        is = new DataInputStream(client.getSocket().getInputStream());
    }

    @Override
    public void run() {
        System.out.println("ClientThreadListener initialized");
        try {
            while (!client.getSocket().isClosed()) {
                int answer = is.read();
                switch (answer) {
                    case Commands.HELLO_MESSAGE:
                        int id = is.read();
                        System.out.println("Game session id: " + id);
                        System.out.println(
                                "       **************\n       Heads or Tails\n       **************\n" +
                                        "- To begin the game enter \"play\"\n- To view games history enter \"history\"\n" +
                                        "- To exit the game enter \"quit\"\n");
                        break;
                    case Commands.SIDE:
                        System.out.println("Enter the side: heads(h) or tails(t)");
                        break;
                    case Commands.BET:
                        System.out.print("Enter your bet: ");
                        client.setCanBet(true);
                        break;
                    case Commands.RESULT:
                        int[] result = new int[2];
                        for (int i = 0; i < 2; i++) {
                            result[i] = is.read();
                        }
                        System.out.println("Result: " + (result[0] == Commands.WIN ?
                                "WIN" : "LOSE") + " | Balance: " + result[1]);
                        break;
                    case Commands.AGAIN:
                        System.out.println("Enter \"play\" to play again");
                        break;
                    case Commands.HISTORY:
                        System.out.println(is.readUTF());
                        break;
                    case Commands.CLOSING_CONNECTION:
                        System.out.println("Closing connection");
                        client.getSocket().close();
                        break;
                    case Commands.ERROR_WRONG_COMMAND:
                        System.out.println("Wrong command. Please try again");
                        break;
                    case Commands.ERROR_NULL_BALANCE:
                        System.out.println("Your balance is zero. The end.");
                        break;
                    case Commands.ERROR_NOT_ENOUGH_MONEY:
                        System.out.println("You have not enough money to make a bet");
                        break;
                    default:
                        is.readUTF();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.err.println("[Error] " + e.getMessage());
            }
        }
    }
}
