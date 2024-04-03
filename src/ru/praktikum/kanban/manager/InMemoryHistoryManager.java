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

    // Удаляемая нода
    private void removeNode(Node node) {
        if (node.prev == null) { // Удаление первой ноды
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (node.next == null) { // Удаление последней ноды
            tail = node.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else { // Удаление ноды из центра
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    private static final int MAX_HISTORY_SIZE = 10;

    private int size;
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        Node newNode = new Node(task, tail, null);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        if (history.contains(task)) {
            history.remove(task);
        }
        history.add(task);
        size++;
        if (size > MAX_HISTORY_SIZE) {
            removeNode(head);
            size--;
        }
    }

    @Override
    public void remove(int id) {
        Node current = head;
        while (current != null) {
            if (current.task.getId() == id) {
                removeNode(current);
                history.remove(current.task);
                size--;
                break;
            }
            current = current.next;
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


