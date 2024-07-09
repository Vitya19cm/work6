import ru.praktikum.kanban.manager.*;
import ru.praktikum.kanban.model.*;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создание менеджера задач
        TaskManager taskManager = new FileBackedTaskManager(new File("tasks.txt"));

        // Создание задач
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        Subtask subtask = new Subtask(3, "Subtask 1", "Subtask Description 1", TaskStatus.NEW, 0);
        Epic epic = new Epic(4, "Epic 1", "Epic Description 1", TaskStatus.NEW, null); // Исправлен вызов конструктора Epic

        // Добавление задач в менеджер
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(subtask);
        taskManager.createTask(epic);

        // Обновление задачи
        taskManager.updateTask(new Task(1, "Task 1", "Updated Description", TaskStatus.IN_PROGRESS));

        // Удаление задачи
        taskManager.removeTaskById(2);

        // Получение всех задач
        List<Task> allTasks = taskManager.getAllTasks();

        // Вывод всех задач
        for (Task task : allTasks) {
            System.out.println(task);
        }
    }
}

