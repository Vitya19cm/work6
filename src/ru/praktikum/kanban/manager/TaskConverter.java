package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskConverter {

    public static String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task instanceof Subtask) {
            sb.append(((Subtask) task).getEpicId());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            sb.append(epic.getSubtasks().stream().map(Object::toString).collect(Collectors.joining(",")));
        }
        return sb.toString();
    }

    public static Task taskFromString(String value) {
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
                List<Integer> subtasks = Stream.of(parts[5].split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                return new Epic(id, title, description, status, subtasks);
            default:
                throw new IllegalArgumentException("Invalid task type");
        }
    }

    public static String historyToString(List<Task> history) {
        return history.stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    public static List<Integer> historyFromString(String value) {
        return Stream.of(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}

