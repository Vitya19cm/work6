package ru.praktikum.kanban.manager;

import ru.praktikum.kanban.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskConverter {
    public static String taskToString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return "SUBTASK," + task.getId() + "," + task.getTitle() + "," + task.getDescription() + "," + task.getStatus() + ","
                    + subtask.getEpicId() + "," + subtask.getDuration() + "," + subtask.getStartTime();
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
                Duration duration = Duration.parse(parts[6]);
                LocalDateTime startTime = LocalDateTime.parse(parts[7]);
                return new Subtask(id, title, description, status, duration, startTime, epicId);
            case EPIC:
                List<Integer> subtaskIds = Arrays.stream(parts[5].split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                return new Epic(id, title, description, status, subtaskIds);
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




