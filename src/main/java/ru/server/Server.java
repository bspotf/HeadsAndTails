package ru.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private HistoryService history;

    public void start(int port) {
        try (ServerSocket socket = new ServerSocket(port);
             BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket is initialized.");
            history = HistoryService.getHistoryService();
            while (!socket.isClosed()) {
                // Looking for the console commands
                if (br.ready()) {
                    String data = br.readLine();
                    if (data.equalsIgnoreCase("quit")) {
                        System.out.println("Closing server");
                        break;
                    }
                }
                // Waiting for the client connection
                Socket client = socket.accept();
                System.out.println(socket.getInetAddress().getHostName() +
                        " connected");
                ExecutorService service = Executors.newCachedThreadPool();
//                Thread service = new Thread(new ThreadClientHandler(client));
                service.execute(new ThreadClientHandler(client));
                System.out.println("Connection accepted");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
