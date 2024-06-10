import ru.praktikum.kanban.manager.HistoryManager;
import ru.praktikum.kanban.manager.Managers;
import ru.praktikum.kanban.manager.TaskManager;
import ru.praktikum.kanban.model.*;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Создание менеджера задач
        TaskManager taskManager = new ru.praktikum.kanban.manager.InMemoryTaskManager();

        // Создание задач
        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.NEW);
        Subtask subtask = new Subtask(3, "Subtask 1", "Subtask Description 1", TaskStatus.NEW, 0);
        Epic epic = new Epic(4, "Epic 1", "Epic Description 1", TaskStatus.NEW);

        // Добавление задач в менеджер
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(subtask);
        taskManager.createTask(epic);

        // Обновление задачи
        taskManager.updateTask(new Task(1, "Task 1", "Updated Description", TaskStatus.IN_PROGRESS));

        // Удаление задачи
        taskManager.removeTaskById(2);

        // Получение всех задач определенного типа
        List<Task> allTasks = taskManager.getAllTasksByType();
        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        List<Epic> allEpics = taskManager.getAllEpics();

        // Вывод всех задач
        for (Task task : allTasks) {
            System.out.println(task);
        }

        // Вывод всех подзадач
        for (Subtask st : allSubtasks) {
            System.out.println(st);
        }

        // Вывод всех эпиков
        for (Epic e : allEpics) {
            System.out.println(e);
        }
    }
}







