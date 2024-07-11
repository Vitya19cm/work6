package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.io.File;
import java.util.List;

public class Managers {
    private static TaskManager defaultTaskManager;
    private static HistoryManager defaultHistoryManager;

    public static TaskManager getDefaultTaskManager() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager();
        }
        return defaultTaskManager;
    }

    public static TaskManager getFileBackedTaskManager(String filename) {
        File file = new File(filename);
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        ((FileBackedTaskManager) taskManager).loadFromFile(file); // Вызываем loadFromFile после создания объекта
        return taskManager;
    }


}
