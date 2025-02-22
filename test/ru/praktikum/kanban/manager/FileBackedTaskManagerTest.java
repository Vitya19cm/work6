package ru.praktikum.kanban.manager;

import org.junit.Test;
import ru.praktikum.kanban.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testFileBackedTaskManager() throws IOException {
        // Создаем временный файл для тестирования
        File file = Files.createTempFile("kanban", ".txt").toFile();

        // Создаем файловый менеджер и добавляем задачи, эпики и подзадачи
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic(3, "Epic 1", "Description 3", TaskStatus.NEW, List.of());
        manager.createTask(epic1);

        Subtask subtask1 = new Subtask(4, "Subtask 1", "Description 4", TaskStatus.NEW, epic1.getId());
        manager.createTask(subtask1);

        // Проверяем историю до сохранения в файл
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        // Создаем новый файловый менеджер из того же файла и проверяем корректность восстановления состояния
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем задачи
        assertEquals(manager.getAllTasks(), newManager.getAllTasks());

        // Проверяем эпики
        assertEquals(manager.getAllEpics(), newManager.getAllEpics());

        // Проверяем подзадачи
        assertEquals(manager.getAllSubtasks(), newManager.getAllSubtasks());

        // Проверяем историю
        assertEquals(manager.getHistory(), newManager.getHistory());

        // Удаляем временный файл после тестирования
        file.delete();
    }
}




