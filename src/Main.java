import manager.Manager;
import task.EpicTask;
import task.Status;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {

        var manager = new Manager();
        var index = 0;

        Task task1 = new Task("Heading1", "desc", Status.NEW);
        Task task2 = new Task("Heading2", "desc", Status.NEW);
        Task task3 = new Task("Heading3", "desc", Status.NEW);

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);

        for (var row : manager.getTasks()) {
            System.out.println((index + 1) + "я задача: " + row.getTitle());
            index++;
        }
        index = 0;
        System.out.println("\n");

        EpicTask epicTask1 = new EpicTask("EpicTitle1", "EpicDesc");
        EpicTask epicTask2 = new EpicTask("EpicTitle2", "EpicDesc");
        EpicTask epicTask3 = new EpicTask("EpicTitle3", "EpicDesc");

        manager.addNewEpic(epicTask1);
        manager.addNewEpic(epicTask2);
        manager.addNewEpic(epicTask3);

        for (var row : manager.getEpicTask()) {
            System.out.println((index + 1) + "й эпик имеет имя: " + row.getTitle());
            System.out.println((index + 1) + "й эпик при создании имеет статус: " + row.getStatus());
            index++;
        }
        index = 0;
        System.out.println("\n");

        SubTask subTask1 = new SubTask("Heading SubTask1", "Subtask desc", Status.NEW);
        SubTask subTask2 = new SubTask("Heading SubTask2", "Subtask desc", Status.NEW);
        SubTask subTask3 = new SubTask("Heading SubTask3", "Subtask desc", Status.NEW);
        SubTask subTask4 = new SubTask("Heading SubTask4", "Subtask desc", Status.NEW);
        SubTask subTask5 = new SubTask("Heading SubTask5", "Subtask desc", Status.NEW);

        manager.addNewSubtask(subTask1, epicTask1.getId());
        manager.addNewSubtask(subTask2, epicTask1.getId());
        manager.addNewSubtask(subTask3, epicTask1.getId());
        manager.addNewSubtask(subTask4, epicTask2.getId());
        manager.addNewSubtask(subTask5, epicTask3.getId());

        for (var row : manager.getSubtasks()) {
            System.out.println((index + 1) + " ЭПИК");
            System.out.println((index + 1) + "я SubTask имеет заголовок: " + row.getTitle());
            System.out.println((index + 1) + "я SubTask входит в состав Epic: " + row.getEpicId());
            index++;
        }
        index = 0;
        System.out.println("\n");


        for (var row : manager.getSubtasks()) {
            System.out.println((index + 1) + " ЭПИК");
            System.out.println((index + 1) + "я SubTask имеет заголовок: " + row.getTitle());
            System.out.println((index + 1) + "я SubTask входит в состав Epic: " + row.getEpicId());
            System.out.println((index + 1) + "я SubTask статус: " + row.getStatus());
            index++;
        }
        index = 0;
        System.out.println("\n");

        subTask1.setStatus(Status.DONE);
        manager.updateSubtask(subTask1);
        subTask2.setStatus(Status.NEW);
        manager.updateSubtask(subTask2);
        subTask3.setStatus(Status.NEW);
        manager.updateSubtask(subTask3);

        for (var row : manager.getEpicTask()) {
            System.out.println((index + 1) + "й эпик сейчас имеет статус: " + row.getTitle());
            System.out.println((index + 1) + "й эпик сейчас имеет статус: " + row.getStatus());
            System.out.println("в эпике задач: " + manager.getEpicSubtasks(row.getId()).size());
            index++;
        }
        index = 0;
        System.out.println("\n");

        manager.deleteSubtask(subTask1.getId());

        for (var row : manager.getEpicTask()) {
            System.out.println((index + 1) + "й эпик сейчас имеет статус: " + row.getTitle());
            System.out.println((index + 1) + "й эпик сейчас имеет статус: " + row.getStatus());
            System.out.println("в эпике задач: " + manager.getEpicSubtasks(row.getId()).size());
            index++;
        }
    }
}
