package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private final Map<Integer, Node> historyMap;

    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
        Node newNode = new Node(task, tail, null);
        if (tail == null) { // Добавление первой ноды
            head = newNode;
        } else { // Добавление не первой ноды
            tail.next = newNode;
        }
        tail = newNode;
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





