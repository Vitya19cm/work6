package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file; // Файл для сохранения состояния менеджера

    public FileBackedTaskManager(File file) {
        this.file = file;
        // Загрузка данных из файла при создании менеджера
        loadFromFile();
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task); // Вызываем метод создания задачи родительского класса
        saveToFile(); // Сохраняем изменения в файл
        return createdTask;
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask); // Вызываем метод обновления задачи родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId); // Вызываем метод удаления задачи родительского класса
        saveToFile(); // Сохраняем изменения в файл
    }

    // Методы для сохранения и загрузки из файла
    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            // Сохраняем задачи
            for (Task task : getAllTasksByType()) {
                writer.write(taskToString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save manager state to file", e);
        }
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return; // Если файл не существует, выходим из метода
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = taskFromString(line);
                if (task != null) {
                    createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Failed to load manager state from file", e);
        }
    }

    // Методы для работы с форматом CSV
    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task instanceof Subtask) {
            sb.append(((Subtask) task).getEpicId());
        } else if (task instanceof Epic) {
            sb.append("");
        }
        return sb.toString();
    }

    private Task taskFromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String title = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        switch (type) {
            case TASK:
                return new Task(id, title, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, title, description, status, epicId);
            case EPIC:
                return new Epic(id, title, description, status);
            default:
                return null;
        }
    }
}

