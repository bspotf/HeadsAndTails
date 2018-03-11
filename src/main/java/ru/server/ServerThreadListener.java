package ru.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerThreadListener extends Thread {

    private ServerSocket socket;
    private HistoryService history;

    public ServerThreadListener(ServerSocket socket, HistoryService history) {
        this.history = history;
        this.socket = socket;
    }

    @Override
    public void run() {
        // Looking for the console commands
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (!socket.isClosed()) {
                if (br.ready()) {
                    String data = br.readLine();
                    switch (data) {
                        case "log":
                            try {
                                System.out.print("Session number:");
                                int session = Integer.parseInt(br.readLine());
                                ArrayList<String> sessionHistory = history.getSessionHistory(session);
                                for (String aSessionHistory : sessionHistory) {
                                    System.out.println(aSessionHistory);
                                }
                            } catch (NullPointerException e) {
                                System.out.println("No game with that id");
                            }
                            break;
                        case "quit":
                            System.out.println("Closing server");
                            socket.close();
                            break;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
