package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.manager.HistoryManager;
import ru.praktikum.kanban.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private LinkedList<Task> history;

    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (history.contains(task)) {
            history.remove(task);
        } else if (history.size() >= MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}