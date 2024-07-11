package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.List;

import java.util.List;

public interface HistoryManager {
    // Метод для добавления задачи в историю просмотров
    void add(Task task);

    // Метод для удаления задачи из истории просмотров по её id
    void remove(int id);

    // Метод для получения всей истории просмотров
    List<Task> getHistory();
}

