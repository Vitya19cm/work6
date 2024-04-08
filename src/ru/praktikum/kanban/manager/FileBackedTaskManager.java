package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager implements TaskManager {
    private static final String FILENAME = "tasks.csv";

    // Поле для хранения задач
    private final List<Task> tasks;

    public FileBackedTaskManager() {
        this.tasks = new ArrayList<>();
        loadFromFile();
    }

    // Метод для загрузки задач из файла
    private void loadFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(Task.fromString(line));
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
        }
    }

    // Метод для сохранения задач в файл
    private void saveToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
        saveToFile();
    }

    @Override
    public void remove(int id) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == id) {
                iterator.remove();
                break;
            }
        }
        saveToFile();
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTaskManager();

        // Пример использования
        Task task1 = new Task(1, TaskType.TASK, "Task 1", TaskStatus.NEW, "Description for Task 1", 0);
        Task task2 = new Task(2, TaskType.EPIC, "Epic 1", TaskStatus.IN_PROGRESS, "Description for Epic 1", 0);
        Task task3 = new Task(3, TaskType.SUBTASK, "Subtask 1", TaskStatus.DONE, "Description for Subtask 1", 2);

        ((FileBackedTaskManager) taskManager).add(task1);
        ((FileBackedTaskManager) taskManager).add(task2);
        ((FileBackedTaskManager) taskManager).add(task3);

        System.out.println("All tasks:");
        for (Task task : ((FileBackedTaskManager) taskManager).getAll()) {
            System.out.println(task);
        }
    }

    @Override
    public Task createTask(Task task) {
        return null;
    }

    @Override
    public void updateTask(Task updatedTask) {

    }

    @Override
    public void removeTaskById(int taskId) {

    }

    @Override
    public Task getTaskById(int taskId) {
        return null;
    }

    @Override
    public Epic getEpicById(int epicId) {
        return null;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        return null;
    }

    @Override
    public List<Task> getAllTasksByType() {
        return null;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return null;
    }

    @Override
    public List<Epic> getAllEpics() {
        return null;
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        return null;
    }

    @Override
    public void clearAllTasks() {

    }

    @Override
    public void clearAllSubtasks() {

    }

    @Override
    public void clearAllEpics() {

    }

    @Override
    public List<Task> getHistory() {
        return null;
    }
}
