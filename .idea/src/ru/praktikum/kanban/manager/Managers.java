package ru.praktikum.kanban.manager;

public class Managers {
    private static TaskManager defaultTaskManager;
    private static HistoryManager defaultHistoryManager;

    public static TaskManager getDefaultTaskManager() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager();
        }
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistoryManager() {
        if (defaultHistoryManager == null) {
            defaultHistoryManager = new InMemoryHistoryManager();
        }
        return defaultHistoryManager;
    }
}