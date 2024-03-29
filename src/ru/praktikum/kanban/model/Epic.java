package ru.praktikum.kanban.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasks;

    public Epic(int id, String title, String description, TaskStatus status) {
        super(id, title, description, status);
        this.subtasks = new ArrayList<>();
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subtasks.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}


