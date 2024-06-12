package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = TaskConverter.taskFromString(line);
                if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    epics.put(epic.getId(), epic);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    subtasks.put(subtask.getId(), subtask);
                    Epic epic = epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtask(subtask.getId());
                    }
                } else {
                    tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Failed to load manager state from file", e);
        }
    }


    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        saveToFile();
        return createdTask;
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        saveToFile();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        saveToFile();
    }

    public void clearTasks() {
        tasks.clear();
        saveToFile();
    }

    public void clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask.getId());
            }
        }
        subtasks.clear();
        saveToFile();
    }


    public void clearEpics() {
        epics.clear();
        subtasks.clear();
        saveToFile();
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks.values()) {
                writer.write(TaskConverter.taskToString(task) + "\n");
            }
            for (Epic epic : epics.values()) {
                writer.write(TaskConverter.taskToString(epic) + "\n");
                for (Integer subtaskId : epic.getSubtaskIds()) {
                    Subtask subtask = subtasks.get(subtaskId);
                    if (subtask != null) {
                        writer.write(TaskConverter.taskToString(subtask) + "\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save manager state to file", e);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        fileManager.loadFromFile();
        return fileManager;
    }
}











