package manager;

import task.*;
import java.time.ZoneId;
import java.util.*;
import utilites.Managers;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final TreeMap<String, Task> tasks = new TreeMap<>();
    protected final TreeMap<String, SubTask> subTasks = new TreeMap<>();
    protected final TreeMap<String, EpicTask> epicTasks = new TreeMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<EpicTask> getEpicTask() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(String epicId) {
        List<SubTask> epicSubtasks = new ArrayList<>();
        if (epicTasks.containsKey(epicId)) {
            for (String subTaskID : epicTasks.get(epicId).getidSubTasks()) {
                epicSubtasks.add(subTasks.get(subTaskID));
            }
        }
        return epicSubtasks;
    }

    @Override
    public Task getTask(String id) {
        Task task = tasks.get(id);

        if (task == null){
            throw new IllegalArgumentException("Task ID can't be found");
        }

        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public SubTask getSubtask(String id) {
        SubTask subTask = subTasks.get(id);
        if(subTask == null){
            throw new IllegalArgumentException("SubTask ID can't be found");
        }
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public EpicTask getEpic(String id) {
        EpicTask epicTask = epicTasks.get(id);
        if(epicTask == null){
            throw new IllegalArgumentException("Epic ID can't be found");
        }
        if (epicTask != null) {
            historyManager.add(epicTask);
        }
        return epicTask;
    }

    @Override
    public String addNewTask(Task task) {

        String taskId = task.getId();
        if (taskId == null || taskId == "") {
            throw new IllegalArgumentException("Task ID can't be null or empty");
        }

        List<Task> prioritizedTasks = getPrioritizedTasks(TypeTask.Task);
        for (Task existingTask : prioritizedTasks) {
            if (tasksIntersect(task, existingTask)) {
                throw new IllegalArgumentException("The new task conflicts with existing tasks");
            }
        }
        tasks.put(taskId, task);
        return taskId;
    }

    @Override
    public String addNewEpic(EpicTask epic) {
        String epicId = epic.getId();
        if (epicId == null || epicId == "") {
            throw new IllegalArgumentException("Task ID can't be null or empty");
        }

        List<Task> prioritizedTasks = getPrioritizedTasks(TypeTask.EpicTask);
        for (Task existingTask : prioritizedTasks) {
            if (tasksIntersect(epic, existingTask)) {
                throw new IllegalArgumentException("The new task conflicts with existing tasks");
            }
        }

        epicTasks.put(epicId, epic);
        return epicId;
    }

    @Override
    public String addNewSubtask(SubTask subtask, String epicID) {
        String subtaskId = subtask.getId();
        if (subtaskId == null || subtaskId == "") {
            throw new IllegalArgumentException("SubTask ID can't be null or empty");
        }

        List<Task> prioritizedTasks = getPrioritizedTasks(TypeTask.SubTask);
        for (Task existingTask : prioritizedTasks) {
            if (tasksIntersect(subtask, existingTask)) {
                throw new IllegalArgumentException("The new task conflicts with existing tasks");
            }
        }

        subTasks.put(subtaskId, subtask);
        if (epicTasks.containsKey(epicID)) {
            epicTasks.get(epicID).addIDSubTask(subtaskId);
        }
        subTasks.get(subtaskId).setEpicId(epicID);
        updateEpicStatus(epicID);
        return subtaskId;
    }

    @Override
    public void updateTask(Task task) {
        String taskId = task.getId();
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
        }
    }

    @Override
    public void updateEpic(EpicTask epic) {
        String epicId = epic.getId();
        String title = epic.getTitle();
        String description = epic.getDescription();
        if (epicTasks.containsKey(epicId)) {
            epicTasks.get(epicId).setTitle(title);
            epicTasks.get(epicId).setDescription(description);
        }
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        String subtaskId = subtask.getId();
        if (subTasks.containsKey(subtaskId)) {
            subTasks.put(subtaskId, subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteTask(String id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(String epicID) {
        List<String> subtaskIds = new ArrayList<>(epicTasks.get(epicID).getidSubTasks());
        for (String subtaskId : subtaskIds) {
            deleteSubtask(subtaskId);
        }
        epicTasks.remove(epicID);
        historyManager.remove(epicID);
    }

    @Override
    public void deleteSubtask(String id) {
        var epicID = subTasks.get(id).getEpicId();
        epicTasks.get(epicID).removeSubtask(id);
        subTasks.remove(subTasks.get(id));
        updateEpicStatus(epicID);
        historyManager.remove(id);
    }

    @Override
    public void deleteTasks() {
        for (var task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks(String epicID) {
        for (var subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
        updateEpicStatus(epicID);
    }

    @Override
    public void deleteEpics() {
        for (var subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        for (var epicTask : epicTasks.values()) {
            historyManager.remove(epicTask.getId());
        }
        epicTasks.clear();
        subTasks.clear();
    }

    private void updateEpicStatus(String epicId) {
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
    }

    public void printViewedTask() {
        var history = this.getHistory();

        var typeTask = history.stream()
                .filter(task -> task.getClass() == Task.class)
                .collect(Collectors.toList());
        System.out.println("Задач просмотрено: " + typeTask.size());

        var typeEpic = history.stream()
                .filter(task -> task.getClass() == EpicTask.class)
                .collect(Collectors.toList());

        System.out.println("Эпиков просмотрено: " + typeEpic.size());

        var typeSubtask = history.stream()
                .filter(task -> task.getClass() == SubTask.class)
                .collect(Collectors.toList());

        System.out.println("Подзадач просмотрено: " + typeSubtask.size());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getTasks();
    }
    @Override
    public List<Task> getPrioritizedTasks(TypeTask type) {
        List<Task> sortedTasks;
        switch (type) {
            case Task:
                sortedTasks = new ArrayList<>(getTasks());
                break;
            case SubTask:
                sortedTasks = new ArrayList<>(getSubtasks());
                break;
            case EpicTask:
                sortedTasks = new ArrayList<>(getSubtasks());
                break;
            default: sortedTasks = new ArrayList<>();
        }
        sortedTasks.sort(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

        return sortedTasks;
    }

    private boolean tasksIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        ZoneId zone = ZoneId.systemDefault();
        long task1Start = task1.getStartTime().atZone(zone).toInstant().toEpochMilli();
        long task1End = task1Start + task1.getDuration() * 60000;
        long task2Start = task2.getStartTime().atZone(zone).toInstant().toEpochMilli();
        long task2End = task2Start + task2.getDuration() * 60000;

        return task1Start < task2End && task1End > task2Start;
    }
}
