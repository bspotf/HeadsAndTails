package ru.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ThreadServerListener listener;
    private HistoryService history;

    public void start(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server socket is initialized.");
            history = HistoryService.getHistoryService();
            int sessionCounter = 0;
            listener = new ThreadServerListener(socket, history);
            listener.start();
            while (!socket.isClosed()) {
                // Waiting for the client connection
                Socket client = socket.accept();
                System.out.println(socket.getInetAddress().getHostName() +
                        " connected");
                ExecutorService service = Executors.newCachedThreadPool();
//                Thread t = new Thread(new ThreadClientHandler(client, ++sessionCounter, history));
//                t.start();
                service.execute(new ThreadClientHandler(client, ++sessionCounter, history));
                System.out.println("Connection accepted");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
