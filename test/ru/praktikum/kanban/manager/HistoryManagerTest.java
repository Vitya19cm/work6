// HistoryManagerTest.java
package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.TaskStatus;

import java.util.List;

import static org.junit.Assert.*;

public class HistoryManagerTest {

    @Test
    public void testAddAndGetHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    public void testRemoveFromBeginning() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertFalse(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    public void testRemoveFromMiddle() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        Task task3 = new Task(3, "Task 3", "Description 3", TaskStatus.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertFalse(history.contains(task2));
        assertTrue(history.contains(task3));
    }

    @Test
    public void testRemoveFromEnd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertTrue(history.contains(task1));
        assertFalse(history.contains(task2));
    }

}

