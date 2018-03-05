package ru.client;

import ru.commons.Commands;

import java.io.*;
import java.net.Socket;
import java.util.stream.LongStream;

public class ThreadClient implements Runnable {

    private static Socket socket;
    private int numOfAppeals;
    private int intervalBetweenAppeals;
    //    public  String threadName;
    private ThreadClientListener listener;
    private volatile boolean canBet = false;

    public ThreadClient(String host, int port, int appeals, int intBtwAppls) {
        try {
            // Socket for client to interact with server
            socket = new Socket(host, port);
            System.out.println("Client connected to socket");
            Thread.sleep(1000);
            this.numOfAppeals = appeals;
            this.intervalBetweenAppeals = intBtwAppls;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
//                DataInputStream is = new DataInputStream(socket.getInputStream());
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Client os initialized");
            int counter = 0;
            listener = new ThreadClientListener(this);
            listener.start();
            System.out.println("Client is initialized");
            long[] diff = new long[numOfAppeals];
            while (!socket.isClosed() /*&& (counter++ < numOfAppeals)*/) {
                String request = br.readLine();
                switch (request) {
                    case "play":
                        os.write(Commands.PLAY);
                        break;
                    case "history":
                        os.write(Commands.HISTORY);
                        break;
                    case "quit":
                        os.write(Commands.QUIT);
                        break;
                    case "head":
                        os.write(Commands.HEAD);
                        break;
                    case "tail":
                        os.write(Commands.TAIL);
                        break;
                    default:
                        if (canBet == true) {
                            os.writeUTF(request);
                            setCanBet(false);
                        } else {
                            System.out.println("Wrong command. Please try again");
                        }
                        break;
                }
                os.flush();
//                Thread.sleep(intervalBetweenAppeals);
//                long start = System.currentTimeMillis();
//                long end = System.currentTimeMillis();
//                diff[0] = end - start;

            }
            listener.stopListen();
            LongStream.of(diff).average();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        try {
            System.out.println("Closing socket");
            socket.close();
        } catch (IOException e) {
            System.err.println("Socket closing error");
//            e.printStackTrace();
        }
        System.out.println(" DONE");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setCanBet(boolean state) {
        canBet = state;
    }
}
