package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private Map<Integer, Node> historyMap;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
        this.head = new Node(null);
        this.tail = new Node(null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (historyMap.containsKey(taskId)) {
            removeNode(historyMap.get(taskId));
        }

        Node newNode = new Node(task);
        Node prev = tail.prev;
        prev.next = newNode;
        newNode.prev = prev;
        newNode.next = tail;
        tail.prev = newNode;
        historyMap.put(taskId, newNode);

        if (historyMap.size() > MAX_HISTORY_SIZE) {
            removeNode(head.next);
        }
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node current = head.next;
        while (current != tail) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        historyMap.remove(node.task.getId());
    }

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
}

