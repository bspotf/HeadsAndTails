package ru.client;

import ru.commons.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientStressTest extends ClientThread implements Runnable {

    private int id = 0;

    public ClientStressTest(String host, int port, int numOfAppeals, int intervalBetweenAppeals) {
        super(host, port, numOfAppeals, intervalBetweenAppeals);
    }

    @Override
    public void run() {
        try (
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                DataInputStream is = new DataInputStream(socket.getInputStream())) {
            long[] diff = new long[numOfAppeals];

            int counter = 0;
            int failed = 0;
            byte commandState = 0;
            boolean greetingFlag = true;
            while (!socket.isClosed() && (++counter < numOfAppeals)) {
                if (greetingFlag && (is.read() == Commands.HELLO_MESSAGE)) {
                    id = is.read();
                    commandState = 1;
                    greetingFlag = false;
                }
                switch (commandState) {
                    case 1:
                        os.write(Commands.PLAY);
                        break;
                    case 2:
                        os.write(Commands.HEAD);
                        break;
                    case 3:
                        if (canBet) {
                            os.write(Commands.BET);
                            os.writeUTF(String.valueOf(10));
                            setCanBet(false);
                        }
                }
                long start = System.currentTimeMillis();
                if (is.available() != 0) {
                    int answer = is.read();
                    switch (answer) {
                        case Commands.SIDE:
                            commandState = 2;
                            break;
                        case Commands.BET:
                            commandState = 3;
                            setCanBet(true);
                            break;
                        case Commands.RESULT:
                            is.read();
                            break;
                        case Commands.AGAIN:
                            commandState = 1;
                            break;
                    }
                    long end = System.currentTimeMillis();
                    diff[counter - 1] = end - start;
                } else {
                    failed++;
                    diff[counter - 1] = 0;
                }
                os.flush();
                Thread.sleep(intervalBetweenAppeals);
            }
            long avg = 0;
            for (long aDiff : diff) {
                avg += aDiff;
            }
            avg = avg / diff.length;
            System.out.println("ID: " + id + " | Passed: " + (counter - failed) +
                    " | Failed: " + failed + " | Average request time: " +
                    avg + " ms");
        } catch (IOException e) {
            System.err.println("[Error] " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("[Error] No connection to server");
        } catch (InterruptedException e) {
            System.err.println("[Error] " + e.getMessage());
        } finally {
            System.out.println("Closing connection. ID: " + id);
            if (socket != null) {
                disconnect();
            }
        }

    }
}
