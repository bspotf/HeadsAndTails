package ru.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by V on 01.03.2018.
 */
public class Client {

    private static int numberOfUsers;
    private static int intervalBetweenHandles;
    private static int appealsByOneUser;
    private static int port = 8080;
    private final static String host = "localhost";


    public void start(String[] args) throws InterruptedException {
        switch (args.length) {
            case 3:
                numberOfUsers = Integer.parseInt(args[0]);
                appealsByOneUser = Integer.parseInt(args[1]);
                intervalBetweenHandles = Integer.parseInt(args[2]);
                break;
            default:
                numberOfUsers = 1;
                appealsByOneUser = 10;
                intervalBetweenHandles = 1000;
                break;
        }
        System.out.println("Launch with default parameters as:\n " +
                "numberOfUsers = " + numberOfUsers + " | appealsByOneUser = " +
                appealsByOneUser + " | intervalBetweenHandles = " + intervalBetweenHandles);
        ExecutorService client = Executors.newFixedThreadPool(numberOfUsers);
        for (int i = 0; i < numberOfUsers; i++) {
        client.execute(new ThreadClient(host, port, appealsByOneUser,intervalBetweenHandles));
//            Thread client = new Thread(new ThreadClient(host, port, appealsByOneUser,intervalBetweenHandles));
            Thread.sleep(500);
        }
        client.shutdown();
    }
}
