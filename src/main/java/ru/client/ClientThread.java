package ru.client;

import ru.commons.Commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {

    private static Socket socket;
    private int numOfAppeals;
    private int intervalBetweenAppeals;
    private ClientThreadListener listener;
    private volatile boolean canBet = false;

    public ClientThread(String host, int port, int numOfAppeals, int intervalBetweenAppeals) {
        try {
            socket = new Socket(host, port);
            System.out.println("Client connected to socket");
            Thread.sleep(1000);
            this.numOfAppeals = numOfAppeals;
            this.intervalBetweenAppeals = intervalBetweenAppeals;
        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Client os initialized");
            int counter = 0;
            listener = new ClientThreadListener(this);
            listener.start();
            System.out.println("Client is initialized");
            long[] diff = new long[numOfAppeals];
            while (!socket.isClosed() /*&& (counter++ < numOfAppeals)*/) {
                if (br.ready()) {
                    String request = br.readLine();
                    switch (request) {
                        case "play":
                        case "p":
                            os.write(Commands.PLAY);
                            break;
                        case "heads":
                        case "h":
                            os.write(Commands.HEAD);
                            break;
                        case "tails":
                        case "t":
                            os.write(Commands.TAIL);
                            break;
                        case "history":
                        case "-h":
                            os.write(Commands.HISTORY);
                            break;
                        case "quit":
                        case "-q":
                            os.write(Commands.QUIT);
                            break;
                        default:
                            if (canBet == true) {
                                os.write(Commands.BET);
                                os.writeUTF(request);
                                setCanBet(false);
                            } else {
                                System.out.println("Wrong command. Please try again");
                            }
                            break;
                    }
                }
                os.flush();
//                Thread.sleep(intervalBetweenAppeals);
//                long start = System.currentTimeMillis();
//                long end = System.currentTimeMillis();
//                diff[0] = end - start;
            }
            System.out.println("Closing connection");
            listener.interrupt();
//            LongStream.of(diff).average();
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("[Error] No connection to server");
        } finally {
            if (socket != null) {
                disconnect();
            }
        }
    }

    private void disconnect() {
        try {
            System.out.print("Closing socket: ");
            socket.close();
            System.out.println(" DONE");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("[Error] No connection to server");
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setCanBet(boolean state) {
        canBet = state;
    }
}
