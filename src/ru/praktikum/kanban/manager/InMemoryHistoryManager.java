package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private LinkedList<Task> history;
    private Map<Integer, Task> taskMap;

    // Конструктор класса
    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
        this.taskMap = new HashMap<Integer, Task>();
    }

    // Метод для добавления задачи в историю просмотров
    @Override
    public void add(Task task) {
        // Проверка наличия задачи в истории
        if (taskMap.containsKey(task.getId())) {
            remove(task.getId()); // Удаление задачи из истории, если она уже там есть
        }

        // Добавление задачи в конец истории
        history.addLast(task);
        taskMap.put(task.getId(), history.getLast()); // Обновление ссылки на узел в HashMap

        // Проверка на превышение максимального размера истории
        if (history.size() > MAX_HISTORY_SIZE) {
            Task removedTask = history.removeFirst();
            taskMap.remove(removedTask.getId()); // Удаление ссылки на узел из HashMap
        }
    }

    // Метод для удаления задачи из истории просмотров по её id
    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            Task nodeToRemove = taskMap.get(id);
            history.remove(nodeToRemove); // Удаление задачи из списка истории
            taskMap.remove(id); // Удаление ссылки на узел из HashMap
        }
    }

    // Метод для получения всей истории просмотров
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
