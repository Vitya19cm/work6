package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.Subtask;
import ru.praktikum.kanban.model.Task;
import ru.praktikum.kanban.model.TaskStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TaskEqualityTest {

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
    public void testTaskEqualityByContent() {
        Task task1 = new Task(1, "Task", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(1, "Task", "Description 2", TaskStatus.NEW);

        assertEquals("Tasks with the same content should be equal", task1, task2);
    }

    @Test
    public void testTaskInequalityByContent() {
        Task task1 = new Task(1, "Task 1", "Description", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description", TaskStatus.NEW);

        assertNotEquals("Tasks with different ids but same content should not be equal", task1, task2);
    }

    @Test
    public void testSubtaskInequalityByStatus() {
        Subtask subtask1 = new Subtask(1, "Subtask", "Description", TaskStatus.NEW, 0);
        Subtask subtask2 = new Subtask(2, "Subtask", "Description", TaskStatus.IN_PROGRESS, 0);

        assertNotEquals("Subtasks with different status should not be equal", subtask1, subtask2);
    }
}