package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskConverter {
    public static String taskToString(Task task) {
        if (task instanceof Subtask) {
            return "SUBTASK," + task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," + task.getStatus() + "," + ((Subtask) task).getEpicId();
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            List<Integer> subtaskIds = epic.getSubtaskIds();
            String subtasks = subtaskIds.stream().map(Object::toString).collect(Collectors.joining(","));
            return "EPIC," + task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," + task.getStatus() + "," + subtasks;
        } else {
            return "TASK," + task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," + task.getStatus();
        }
    }

    public static Task taskFromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            return null;
        }
        int id = Integer.parseInt(parts[1]);
        String title = parts[2];
        String description = parts[3];
        TaskStatus status = TaskStatus.valueOf(parts[4]);
        switch (TaskType.valueOf(parts[0])) {
            case TASK:
                return new Task(id, title, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, title, description, status, epicId);
            case EPIC:
                List<Integer> subtasks = Arrays.stream(parts[5].split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                return new Epic(id, title, description, status, subtasks);
            default:
                return null;
        }
    }

    public static String historyToString(List<Task> history) {
        return history.stream()
                .map(Task::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public static List<Integer> historyFromString(String historyData) {
        return Arrays.stream(historyData.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}


