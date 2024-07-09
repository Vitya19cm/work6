package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    void updateTask(Task updatedTask);

    void removeTaskById(int taskId);

    Task getTaskById(int taskId);

    List<Task> getAllTasks();

}



