package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected final File file;
    protected static Map<Integer, Task> tasks = null;

    public FileBackedTaskManager(File file) {
        this.file = file;
        this.tasks = new HashMap<>();
        loadFromFile(); // Метод loadFromFile теперь доступен извне
    }

    private void loadFromFile() {
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        saveToFile();
        return createdTask;
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        saveToFile();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        saveToFile();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        saveToFile();
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks.values()) {
                writer.write(TaskConverter.taskToString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save manager state to file", e);
        }
    }

    protected static TaskManager loadFromFile(File file) {

        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = TaskConverter.taskFromString(line);
                if (task != null) {
                    tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Failed to load manager state from file", e);
        }
        return null;
    }


}









