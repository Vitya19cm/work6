package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, Subtask> subtasks;
    private Map<Integer, Epic> epics;
    private HistoryManager historyManager;
    private int taskIdCounter;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = new InMemoryHistoryManager();
        this.taskIdCounter = 1;
    }

    @Override
    public Task createTask(Task task) {
        int taskId = generateTaskId();

        switch (task.getType()) {
            case TASK:
                tasks.put(taskId, task);
                break;
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                int epicId = subtask.getEpicId();
                Epic epic = epics.get(epicId);

                if (epic != null) {
                    epic.addSubtask(taskId);
                    subtasks.put(taskId, subtask);
                    updateEpicStatus(epicId);
                }
                break;
            case EPIC:
                epics.put(taskId, (Epic) task);
                break;
            default:
                throw new IllegalArgumentException("Invalid task type");
        }

        return task;
    }

    @Override
    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    @Override
    public void removeTaskById(int taskId) {
        Task removedTask = tasks.remove(taskId);

        if (removedTask != null) {
            TaskType taskType = removedTask.getType();

            switch (taskType) {
                case SUBTASK:
                    subtasks.remove(taskId);
                    break;
                case EPIC:
                    epics.remove(taskId);
                    break;
            }
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.add(task); // Добавление задачи в историю
        }
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic); // Добавление эпика в историю
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask); // Добавление подзадачи в историю
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasksByType() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return new ArrayList<>();
        }

        List<Subtask> epicSubtasks = new ArrayList<>();
        List<Integer> subtaskIds = epic.getSubtasks();

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }

        return epicSubtasks;
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(epic -> epic.getSubtasks().clear());
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
        }
    }
}

