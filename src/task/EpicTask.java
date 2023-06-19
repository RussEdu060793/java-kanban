package task;

import manager.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<String> idSubTasks;

    public EpicTask(String title, String description, ArrayList<String> idSubTasks) {
        super(title, description, Status.NEW);
        this.idSubTasks = idSubTasks;
    }

    public EpicTask(String title, String description) {
        super(title, description, Status.NEW);
        this.idSubTasks = new ArrayList<>();
    }

    public ArrayList<String> getidSubTasks() {
        return idSubTasks;
    }

    public void addIDSubTask(String link) {
        idSubTasks.add(link);
    }

    public void removeSubtask(String subtaskId) {
        idSubTasks.remove(idSubTasks.indexOf(subtaskId));
    }

    @Override
    public TypeTask getType(){
        return TypeTask.EpicTask;
    }


    public int getDuration(TaskManager taskManager) {
        int totalDuration = 0;
        for (String subtaskId : idSubTasks) {
            SubTask subtask = taskManager.getSubtask(subtaskId);
            totalDuration += subtask.getDuration();
        }
        return totalDuration;
    }

    public LocalDateTime getStartTime(TaskManager taskManager) {
        LocalDateTime earliestTime = LocalDateTime.MAX;
        for (String subtaskId : idSubTasks) {
            SubTask subtask = taskManager.getSubtask(subtaskId);
            if (subtask.getStartTime().isBefore(earliestTime)) {
                earliestTime = subtask.getStartTime();
            }
        }
        return earliestTime;
    }

    public LocalDateTime getEndTime(TaskManager taskManager) {
        return getStartTime(taskManager).plusMinutes(getDuration(taskManager));
    }
}
