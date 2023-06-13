package utilites;

import manager.HistoryManager;
import task.*;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    public static Task fromString(String value) {
        Task task = null;
        String[] fields = value.split(",");
        String id = fields[0];
        TypeTask type = TypeTask.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        String epicId = "";

        if (type == TypeTask.SubTask) {
            epicId = fields[5];
        }

        switch (type) {
            case Task:
                task = new Task(name, description, status);
                task.setId(id);
                break;
            case EpicTask:
                task = new EpicTask(name, description);
                task.setId(id);
                break;
            case SubTask:
                SubTask subTask = new SubTask(name, description, Status.NEW);
                subTask.setId(id);
                subTask.setEpicId(epicId);
                task = subTask;
                break;
        }
        return task;
    }

    public static String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(task.getId()).append(",")
                .append(task.getType()).append(",")
                .append(task.getTitle()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",");
        if (task.getType() == TypeTask.SubTask) {
            SubTask subTask = (SubTask) task;
            sb.append(subTask.getEpicId());
        }
        return sb.toString();
    }

    public static String historyToString(HistoryManager manager) {
        var tasks = manager.getTasks();
        var line = "";
        for (var task : tasks) {
            line = task.getId() + ", ";
        }
        return line;
    }

    public static List<String> historyFromString(String value) {
        var line = value.split(",");
        List<String> listID = new ArrayList<>();
        for (var item : line) {
            if (item.length() > 1) {
                listID.add(item);
            }
        }
        return listID;
    }
}
