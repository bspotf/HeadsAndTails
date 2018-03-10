package ru.client;

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
        for (int i = 0; i < numberOfUsers; i++) {
            Thread client = new Thread(new ClientThread(host, port, appealsByOneUser, intervalBetweenHandles));
            client.start();
            Thread.sleep(500);
        }
    }
}
