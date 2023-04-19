package Manager;

import Task.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager {

    public static Manager shared = new Manager();

    List<Task> listTasks = new ArrayList<Task>();

    public EpicTask  convertingTaskToEpic(Task task){

        EpicTask epicTask = new EpicTask(task.getTitle(),task.getDescription(), task.getId());

        deleteTaskByID(task.getId());
        return epicTask;
    }

    public void printListOfTask(List<Task> listTasks) {

        int index = 0;

        for (var item : listTasks) {
            System.out.println( (index+1) + ": "+item.getTitle());
            index++;
        }
    }

    public void clearListOfTask() {
        listTasks.clear();
        System.out.println("Список задач удален");
    }

    public Task getTaskById(String id) {

        for (var item : listTasks) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public EpicTask getEpicTaskById(String id) {

        for (var item : filterEpic()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void addingTask(Task task){

        listTasks.add(task);
        if (task instanceof SubTask) {
            var taskSubtask = (SubTask) task;
            taskSubtask.epicTask.addSubTask(taskSubtask);
        }
    }

    public void updateEpicTask(Task task, String id){

        for (var i=0; i < listTasks.size();i++){
            if (listTasks.get(i).getId().equals(id)){
                listTasks.set(i,task);
            }
        }
    }

    public void  deleteTaskByID(String id){

        listTasks.remove(getTaskById(id));
    }

    public List<EpicTask> filterEpic() {

        List<EpicTask> epics = new ArrayList<>();

        for (Task task : listTasks) {
            if (task instanceof EpicTask) {
                epics.add((EpicTask) task);
            }
        }
        return epics;
    }

    public int getCountListOfTaskAndEpic(){

        return listTasks.size();
    }

    private List<SubTask> filterSubTask() {

        List<SubTask> subTasks = new ArrayList<>();

        for (Task task : listTasks) {
            if (task instanceof SubTask) {
                subTasks.add((SubTask) task);
            }
        }
        return subTasks;
    }

    public List<Task> filterNotSubTask() {

        List<Task> tasks = new ArrayList<>();

        for (Task task : listTasks) {
            if (!(task instanceof SubTask)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void printNameEpicSubTask(EpicTask epicTask){

         for (var name : epicTask.getSubTasks()){
             System.out.println(name.getTitle());
         }
    }

    public void changeSubTaskStatus(String subTaskID, Task.Status status){

        var subTask = (SubTask) getTaskById(subTaskID);

        subTask.setStatus(status);
        subTask.epicTask.updateEpicStatus();
    }
}
