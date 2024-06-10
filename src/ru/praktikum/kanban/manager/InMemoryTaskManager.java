package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
    }

    @Override
    public Task createTask(Task task) {
        if (task.getId() == 0) {
            int taskId = generateTaskId();
            task.setId(taskId);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    @Override
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    private int generateTaskId() {
        int maxId = tasks.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
        return maxId + 1;
    }
}







