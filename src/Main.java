import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.EpicTask;
import task.Status;
import task.SubTask;
import task.Task;
import utilites.Managers;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var taskManager = Managers.getDefault();

        Task task1 = new Task("JustTask1", "decription 1", Status.NEW);
        Task task2 = new Task("JustTask2", "decription 2", Status.NEW);
        Task task3 = new Task("JustTask3", "decription 2", Status.NEW);
        Task task4 = new Task("JustTask4", "decription 2", Status.NEW);
        Task task5 = new Task("JustTask5", "decription 2", Status.NEW);
        Task task6 = new Task("JustTask6", "decription 2", Status.NEW);
        Task task7 = new Task("JustTask7", "decription 2", Status.NEW);
        Task task8 = new Task("JustTask8", "decription 2", Status.NEW);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);
        taskManager.addNewTask(task4);
        taskManager.addNewTask(task5);
        taskManager.addNewTask(task6);
        taskManager.addNewTask(task7);
        taskManager.addNewTask(task8);

        EpicTask epicTask1 = new EpicTask("Epic1", "epicDesc1");
        taskManager.addNewEpic(epicTask1);

        SubTask subTask1 = new SubTask("subTask1", "decription 1", Status.NEW);
        taskManager.addNewSubtask(subTask1, epicTask1.getId());

        SubTask subTask2 = new SubTask("subTask2", "decription 2", Status.NEW);
        taskManager.addNewSubtask(subTask2, epicTask1.getId());

        SubTask subTask3 = new SubTask("subTask3", "decription 3", Status.NEW);
        taskManager.addNewSubtask(subTask3, epicTask1.getId());

        EpicTask emptyEpicTask = new EpicTask("emptyEpicTask", "emptyEpicTaskDesc");
        taskManager.addNewEpic(emptyEpicTask);

        taskManager.getTask(taskManager.getTask(task1.getId()).getId());
        taskManager.getTask(taskManager.getTask(task2.getId()).getId());
        taskManager.getTask(taskManager.getTask(task3.getId()).getId());
        taskManager.getTask(taskManager.getTask(task4.getId()).getId());
        taskManager.getTask(taskManager.getTask(task5.getId()).getId());
        taskManager.getTask(taskManager.getTask(task6.getId()).getId());
        taskManager.getTask(taskManager.getTask(task7.getId()).getId());
        taskManager.getTask(taskManager.getTask(task8.getId()).getId());

        taskManager.getEpic(taskManager.getEpic(epicTask1.getId()).getId());
        taskManager.getEpic(taskManager.getEpic(emptyEpicTask.getId()).getId());

        taskManager.getSubtask(taskManager.getSubtask(subTask1.getId()).getId());
        taskManager.getSubtask(taskManager.getSubtask(subTask2.getId()).getId());
        taskManager.getSubtask(taskManager.getSubtask(subTask3.getId()).getId());

        printHistory(taskManager);

        taskManager.deleteTask(task1.getId());
        printHistory(taskManager);

        taskManager.deleteEpic(taskManager.getEpic(epicTask1.getId()).getId());
        printHistory(taskManager);
    }

    static void printHistory(TaskManager taskManager) {
        List<Task> tasks = taskManager.getHistory();
        System.out.println("Просмотренные задачи");
        for (var task : tasks) {
            System.out.println(task.getTitle());
        }
        System.out.println("            ");
    }
}
