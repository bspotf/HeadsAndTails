package ru.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerThreadListener listener;
    private HistoryService history;

    public void start(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server socket is initialized.");
            history = HistoryService.getHistoryService();
            int sessionCounter = 0;
            listener = new ServerThreadListener(socket, history);
            listener.start();
            while (!socket.isClosed()) {
                // Waiting for the client connection
                Socket client = socket.accept();
                System.out.println(socket.getInetAddress().getHostName() +
                        " connected");
                Thread session = new Thread(new ServerClientHandler(client, ++sessionCounter, history));
                session.start();
                System.out.println("Connection accepted");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
