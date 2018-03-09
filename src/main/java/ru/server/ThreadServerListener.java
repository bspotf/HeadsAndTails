package ru.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by V on 10.03.2018.
 */
public class ThreadServerListener extends Thread {

    private ServerSocket socket;
    private HistoryService history;

    public ThreadServerListener(ServerSocket socket, HistoryService history) {
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
                            System.out.print("Session number:");
                            int session = br.read();
                            ArrayList<String> sessionHistory = history.getSessionHistory(session);
                            Iterator<String> it = sessionHistory.iterator();
                            while (it.hasNext())
                                System.out.println(it.next());
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
