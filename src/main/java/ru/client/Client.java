package ru.client;

public class Client {

    private final static String host = "localhost";

    public void start(String[] args) throws InterruptedException {
        int appealsByOneUser;
        int numberOfUsers;
        int intervalBetweenHandles;
        switch (args.length) {
            case 3:
                numberOfUsers = Integer.parseInt(args[0]);
                appealsByOneUser = Integer.parseInt(args[1]);
                intervalBetweenHandles = Integer.parseInt(args[2]);
                break;
            default:
                numberOfUsers = 1;
                appealsByOneUser = 10;
                intervalBetweenHandles = 100;
                break;
        }
        System.out.println("Launch with default parameters as:\n " +
                "numberOfUsers = " + numberOfUsers + " | appealsByOneUser = " +
                appealsByOneUser + " | intervalBetweenHandles = " + intervalBetweenHandles);
        for (int i = 0; i < numberOfUsers; i++) {
//            Uncomment to play manually
//            Thread client = new Thread(new ClientThread(host, port, appealsByOneUser, intervalBetweenHandles));
            int port = 8080;
            Thread client = new Thread(new ClientStressTest(host, port, appealsByOneUser, intervalBetweenHandles));
            client.start();
            Thread.sleep(500);
        }
    }
}
