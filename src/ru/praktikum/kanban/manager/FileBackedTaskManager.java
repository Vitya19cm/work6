package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file; // Эти переменные нужны, так как используются в коде. Убирая любую из них возникает ошибка компиляции
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private int taskIdCounter;

    private FileBackedTaskManager(File file) {
        this.file = file;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = new InMemoryHistoryManager();
        this.taskIdCounter = 1;
        loadFromFile();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
        return createdTask; // Возвращаем созданную задачу
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId); // Вызываем метод из родительского класса
        if (task != null) {
            historyManager.add(task); // Добавляем задачу в историю
        }
        return task; // Возвращаем задачу
    }
    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask); // Добавление подзадачи в историю
            saveToFile(); // Сохраняем изменения в файл
        }
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks(); // Вызываем метод из родительского класса
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics(); // Вызываем метод из родительского класса
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        return super.getSubtasksOfEpic(epicId); // Вызываем метод из родительского класса
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks(); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks(); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics(); // Вызываем метод из родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory(); // Возвращаем историю из объекта historyManager
    }

    @Override
    public List<Task> getAllTasks() { // Этот метод нужен, так как если убираем его возникает ошибка компиляции. Компилятор просит делать абстрактный метод вместо него. Нам это не нужно
        return new ArrayList<>(tasks.values());
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks.values()) {
                writer.write(TaskConverter.taskToString(task) + "\n");
            }
            writer.write(TaskConverter.historyToString(getHistory())); // Запись истории в файл
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save manager state to file", e);
        }
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = TaskConverter.taskFromString(line);
                if (task != null) {
                    createTask(task);
                }
            }
            String historyData = reader.readLine();
            List<Integer> history = TaskConverter.historyFromString(historyData);
            for (int taskId : history) {
                historyManager.add(getTaskById(taskId));
            }
            updateTaskIdCounter(); // Обновление счетчика идентификаторов
        } catch (IOException e) {
            throw new ManagerLoadException("Failed to load manager state from file", e);
        }
    }

    private void updateTaskIdCounter() {
        int maxId = tasks.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
        maxId = Math.max(maxId, subtasks.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(0));
        maxId = Math.max(maxId, epics.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(0));
        taskIdCounter = maxId + 1;
    }

    private int generateTaskId() {
        return taskIdCounter++;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean allSubtasksDone = true;

        for (int subtaskId : epic.getSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null && subtask.getStatus() != TaskStatus.DONE) {
                allSubtasksDone = false;
                break;
            }
        }

        if (allSubtasksDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}




