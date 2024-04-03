package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.*;

import java.util.List;

import static org.junit.Assert.*;

public class TaskManagerTest {

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

}
