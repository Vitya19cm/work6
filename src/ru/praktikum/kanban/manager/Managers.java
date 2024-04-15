package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.List;

public class Managers {
    private static TaskManager defaultTaskManager;
    private static HistoryManager defaultHistoryManager;

    public static TaskManager getDefaultTaskManager() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager() {
                @Override
                public List<Task> getAllTasks() {
                    return null;
                }
            };
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