package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
