package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    protected Map<Integer, Epic> epics;
    protected HistoryManager historyManager;
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
        task.setId(taskId);

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
                    Subtask subtask = (Subtask) removedTask;
                    Epic epic = epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.removeSubtask(taskId);
                    }
                    subtasks.remove(taskId);
                    break;
                case EPIC:
                    Epic removedEpic = (Epic) removedTask;
                    for (int subtaskId : removedEpic.getSubtaskIds()) {
                        subtasks.remove(subtaskId);
                        historyManager.remove(subtaskId);
                    }
                    epics.remove(taskId);
                    break;
                case TASK:
                    tasks.remove(taskId);
                    break;
            }
            historyManager.remove(taskId);
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

    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic); // Добавление эпика в историю
        }
        return epic;
    }

    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask); // Добавление подзадачи в историю
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return new ArrayList<>();
        }

        List<Subtask> epicSubtasks = new ArrayList<>();
        List<Integer> subtaskIds = epic.getSubtaskIds();

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }

        return epicSubtasks;
    }


    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    public void clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
        }
    }

    public void clearEpics() {
        for (Epic epic : epics.values()) {
            for (int subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(subtaskId);
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateTaskId() {
        return taskIdCounter++;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean allSubtasksDone = true;

        for (int subtaskId : epic.getSubtaskIds()) {
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











