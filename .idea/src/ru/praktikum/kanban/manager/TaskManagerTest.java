package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.*;

import static org.junit.Assert.*;

public class TaskManagerTest {

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
}

