package ru.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryService {

    private static HistoryService instance = new HistoryService();
    private static Map<Integer, ArrayList<String>> history;

    private HistoryService() {
        history = new HashMap<>();
    }

    public static HistoryService getHistoryService() {
        return instance;
    }

    public ArrayList<String> getSessionHistory(Integer sessionId) {
        if (!history.isEmpty()) {
            return history.get(sessionId);
        } else {
            return null;
        }
    }

    public void add(int sessionId, ArrayList<String> session) {
        history.put(sessionId, session);
    }

}
