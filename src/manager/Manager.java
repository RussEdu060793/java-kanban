package manager;
import task.Status;
import java.util.HashMap;
import java.util.Map;
import task.EpicTask;
import task.Task;
import task.SubTask;
import java.util.ArrayList;
import java.util.List;


public class Manager {

    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, SubTask> subTasks = new HashMap<>();
    private final Map<String, EpicTask> epicTasks = new HashMap<>();

    public List<Task> getTasks(){
        return new ArrayList<>(tasks.values());
    }

    public List<SubTask> getSubtasks(){
        return new ArrayList<>(subTasks.values());
    }

    public List<EpicTask> getEpicTask() {
        return new ArrayList<>(epicTasks.values());
    }

    public List<SubTask> getEpicSubtasks(String epicId) {
        List<SubTask> epicSubtasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId().equals(epicId)) {
                epicSubtasks.add(subTask);
            }
        }
        return epicSubtasks;
    }

    public Task getTask(String id) {
        return tasks.get(id);
    }

    public SubTask getSubtask(String id) {
        return subTasks.get(id);
    }

    public EpicTask getEpic(String id) {
        return epicTasks.get(id);
    }

    public String addNewTask(Task task) {
        String taskId = task.getId();
        tasks.put(taskId, task);
        return taskId;
    }

    public String addNewEpic(EpicTask epic) {
        String epicId = epic.getId();
        epicTasks.put(epicId, epic);
        return epicId;
    }

    public String addNewSubtask(SubTask subtask) {
        String subtaskId = subtask.getId();
        subTasks.put(subtaskId, subtask);
        return subtaskId;
    }

    public void updateTask(Task task) {
        String taskId = task.getId();
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
        }
    }

    public void updateEpic(EpicTask epic) {
        String epicId = epic.getId();
        if (epicTasks.containsKey(epicId)) {
            epicTasks.put(epicId, epic);
        }
    }

    public void updateSubtask(SubTask subtask) {
        String subtaskId = subtask.getId();
        if (subTasks.containsKey(subtaskId)) {
            subTasks.put(subtaskId, subtask);
        }
    }

    // методы удаления
    public void deleteTask(String id) {
        tasks.remove(id);
    }

    public void deleteEpic(String id) {
        epicTasks.remove(id);
    }

    public void deleteSubtask(String id) {
        subTasks.remove(id);
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        subTasks.clear();
    }

    public void deleteEpics() {
        epicTasks.clear();
    }

    public void updateEpicStatus(String epicId) {
        EpicTask epic = epicTasks.get(epicId);
        if (epic != null) {
            List<SubTask> epicSubtasks = getEpicSubtasks(epicId);
            boolean allDone = true;
            boolean allNew = true;
            boolean someDone = false;
            for (SubTask subTask : epicSubtasks) {
                if (subTask.getStatus() == Status.DONE) {
                    someDone = true;
                    allNew = false;
                } else if (subTask.getStatus() == Status.NEW) {
                    allDone = false;
                } else {
                    allDone = false;
                    allNew = false;
                }
            }

            if (allNew) {
                epic.setStatus(Status.NEW);
            } else if (allDone) {
                epic.setStatus(Status.DONE);
            } else if (someDone) {
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
        updateEpic(epic);
    }

    public void addSubtaskIntoEpic(String epicID, String subTaskID){
        this.subTasks.get(subTaskID).setEpicId(epicID);
    }
}
