package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.*;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class TaskManagerTest {
    private HistoryManager historyManager;

    @Test
    public void testTaskEqualityById() {
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);

        assertEquals("Tasks with the same id should be equal", task1, task1);
        assertNotEquals("Tasks with different ids should not be equal", task1, task2);
    }

    @Test
    public void testSubtaskEqualityById() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask Description 1", TaskStatus.NEW, 0);
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask Description 2", TaskStatus.NEW, 0);

        assertEquals("Subtasks with the same id should be equal", subtask1, subtask1);
        assertNotEquals("Subtasks with different ids should not be equal", subtask1, subtask2);
    }

    @Test
    public void testEpicCannotBeAddedAsSubtaskToItself() {
        Epic epic = new Epic(1, "Epic 1", "Epic Description 1", TaskStatus.NEW);

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertFalse("Epic cannot be added as subtask to itself",
                taskManager.createTask(epic).getType() == TaskType.SUBTASK);
    }

    @Test
    public void testSubtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask(1, "Subtask 1", "Subtask Description 1", TaskStatus.NEW, 1);

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertFalse("Subtask cannot be its own epic",
                taskManager.createTask(subtask).getType() == TaskType.EPIC);
    }

    @Test
    public void testManagersAlwaysReturnInitializedInstances() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        assertNotNull("Task manager should not be null", taskManager);

        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        assertNotNull("History manager should not be null", (TaskManager) historyManager);
    }

    @Test
    public void testInMemoryTaskManagerAddTasksAndFindById() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);

        Subtask subtask = new Subtask(2, "Subtask 1", "Subtask Description 1", TaskStatus.NEW, 0);
        taskManager.createTask(subtask);

        Epic epic = new Epic(3, "Epic 1", "Epic Description 1", TaskStatus.NEW);
        taskManager.createTask(epic);

        assertEquals("Task should be found by id", task, taskManager.getTaskById(1));
        assertEquals("Subtask should be found by id", subtask, taskManager.getTaskById(2));
        assertEquals("Epic should be found by id", epic, taskManager.getTaskById(3));
    }

    @Test
    public void testNoConflictBetweenSpecifiedAndGeneratedTaskIds() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals("Specified task id should not be changed", task1, taskManager.getTaskById(1));
        assertEquals("Generated task id should be unique", task2, taskManager.getTaskById(2));
    }

    @Test
    public void testTasksAddedToHistoryManagerRetainPreviousVersions() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task updatedTask1 = new Task(1, "Task 1", "Updated Description", TaskStatus.IN_PROGRESS);

        historyManager.add(task1);
        historyManager.add(updatedTask1);

        assertEquals("History should retain previous versions of tasks", task1, historyManager.getHistory().get(0));
    }
    // Тест добавления задачи в историю
    @Test
    public void testAddToHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task));
    }

    // Тест удаления задачи из истории
    @Test
    public void testRemoveFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        historyManager.add(task);
        assertTrue(historyManager.getHistory().contains(task));
        historyManager.remove(1);
        assertFalse(historyManager.getHistory().contains(task));
    }

    // Тест обновления задачи
    @Test
    public void testUpdateTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        taskManager.createTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
    }

    // Тест получения всех задач определенного типа
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

    // Тест получения всех подзадач
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

    // Тест получения всех эпиков
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

    // Тест получения всех подзадач конкретного эпика
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
    @Before
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddAndGetHistory() {
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