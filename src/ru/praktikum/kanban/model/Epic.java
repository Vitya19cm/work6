package ru.praktikum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.praktikum.kanban.manager.FileBackedTaskManager;

public class Epic extends Task {
    private List<Integer> subtaskIds;
    private LocalDateTime endTime;
    private FileBackedTaskManager taskManager;

    public Epic(int id, String name, String description, TaskStatus status, List<Integer> subtaskIds) {
        super(id, name, description, status);
        this.subtaskIds = new ArrayList<>(subtaskIds);
        this.taskManager = taskManager;
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
        updateTimesAndDuration();
    }

    public void removeSubtask(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
        updateTimesAndDuration();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public Duration getDuration() {
        return subtaskIds.stream()
                .map(subtaskId -> {
                    Subtask subtask = taskManager.getSubtaskById(subtaskId); // Использование экземпляра FileBackedTaskManager
                    return subtask != null ? subtask.getDuration() : Duration.ZERO;
                })
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        return subtaskIds.stream()
                .map(subtaskId -> {
                    Subtask subtask = taskManager.getSubtaskById(subtaskId); // Использование экземпляра FileBackedTaskManager
                    return subtask != null ? subtask.getStartTime() : null;
                })
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return subtaskIds.stream()
                .map(subtaskId -> {
                    Subtask subtask = taskManager.getSubtaskById(subtaskId); // Использование экземпляра FileBackedTaskManager
                    return subtask != null ? subtask.getEndTime() : null;
                })
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private void updateTimesAndDuration() {
        this.duration = getDuration();
        this.startTime = getStartTime();
        this.endTime = getEndTime();
    }
}






