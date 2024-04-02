package ru.praktikum.kanban.manager;

import org.junit.Before;
import org.junit.Test;
import ru.praktikum.kanban.model.*;

import java.util.List;

import static org.junit.Assert.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @Before
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddToHistory() {
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task));
    }

    @Test
    public void testRemoveFromHistory() {
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task));
        historyManager.remove(1);
        assertFalse(historyManager.getHistory().contains(task));
    }

    @Test
    public void testUpdateTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
    }

    @Test
    public void testGetAllTasksByType() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getAllTasksByType();
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    public void testGetAllSubtasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Description 1", TaskStatus.NEW, 0);
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Description 2", TaskStatus.IN_PROGRESS, 0);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    public void testGetAllEpics() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(1, "Epic 1", "Description 1", TaskStatus.NEW);
        Epic epic2 = new Epic(2, "Epic 2", "Description 2", TaskStatus.IN_PROGRESS);
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
        List<Epic> epics = taskManager.getAllEpics();
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    public void testGetSubtasksOfEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(1, "Epic", "Epic Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Description 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Description 2", TaskStatus.IN_PROGRESS, 1);
        taskManager.createTask(epic);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        List<Subtask> epicSubtasks = taskManager.getSubtasksOfEpic(1);
        assertTrue(epicSubtasks.contains(subtask1));
        assertTrue(epicSubtasks.contains(subtask2));
    }
}
