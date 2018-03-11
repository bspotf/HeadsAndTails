package ru.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void start(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server socket is initialized.");
            HistoryService history = HistoryService.getHistoryService();
            int sessionCounter = 0;
            ServerThreadListener listener = new ServerThreadListener(socket, history);
            listener.start();
            while (!socket.isClosed()) {
                Socket client = socket.accept();
                System.out.println(socket.getInetAddress().getHostName() + " connected");
                Thread session = new Thread(new ServerClientHandler(client, ++sessionCounter, history));
                session.start();
                System.out.println("Connection accepted");

            }

        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        }

    }
}
