package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private Map<Integer, Node> historyMap;

    public InMemoryHistoryManager() {
        this.head = null;
        this.tail = null;
        this.historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Node newNode = new Node(task);
        if (tail == null) { // Добавление первой ноды
            head = newNode;
            tail = newNode;
        } else { // Добавление не первой ноды
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = historyMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            historyMap.remove(id);
        }
    }

    private void removeNode(Node node) {
        if (node.prev == null) { // Удаление первой ноды (head)
            head = node.next;
            if (head != null) head.prev = null; // Обнуляем ссылку на предыдущую ноду для новой первой ноды
        } else if (node.next == null) { // Удаление последней ноды (tail)
            tail = node.prev;
            if (tail != null) tail.next = null; // Обнуляем ссылку на следующую ноду для нового хвоста
        } else { // Удаление ноды из центра
            node.prev.next = node.next; // Даем ссылку предыдущей ноде на следующую за удаляемой
            node.next.prev = node.prev; // Даем ссылку следующей ноде на предыдущую за удаляемой
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }
}


