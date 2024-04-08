package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;
import java.util.List;

public interface TaskManager {
    void add(Task task);

    void remove(int id);

    List<Task> getAll();

    Task createTask(Task task);
    void updateTask(Task updatedTask);
    void removeTaskById(int taskId);
    Task getTaskById(int taskId);
    Epic getEpicById(int epicId); // Новый метод для получения эпика по ID
    Subtask getSubtaskById(int subtaskId); // Новый метод для получения подзадачи по ID
    List<Task> getAllTasksByType();
    List<Subtask> getAllSubtasks();
    List<Epic> getAllEpics();
    List<Subtask> getSubtasksOfEpic(int epicId);
    void clearAllTasks();
    void clearAllSubtasks();
    void clearAllEpics();
    List<Task> getHistory();
}


