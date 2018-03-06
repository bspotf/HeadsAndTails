package ru.server;

import java.util.ArrayList;

public class HistoryService {

    private static volatile HistoryService historyService;
    public static ArrayList<ArrayList<String>> history;


    private HistoryService(ArrayList<ArrayList<String>> history) {
        this.history = history;
    }


    public static HistoryService getHistoryService() {
        if (historyService == null) {
            synchronized (HistoryService.class) {
                if (historyService == null) {
                    historyService = new HistoryService(history);
                }
            }
        }
        return historyService;
    }

    public void addToHistory(int sessionId, ArrayList<String> session) {
        history.add(sessionId, session);
    }

}
