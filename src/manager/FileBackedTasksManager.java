package manager;
import exepctions.ManagerLoadException;
import task.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String filePath;

    public FileBackedTasksManager(String filePath) throws ManagerLoadException {
        this.filePath = filePath;
        try {
            if (Files.exists(Paths.get(filePath))) {
                loadFromFile(filePath);
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Error occurred while loading manager state.", e);
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (EpicTask epic : getEpicTask()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (SubTask subTask : getSubtasks()) {
                writer.write(taskToString(subTask));
                writer.newLine();
            }

            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.length()>1){
                    Task task = fromString(line);
                    if (task instanceof EpicTask) {
                        addNewEpicNoSave((EpicTask) task);
                    } else if (task instanceof SubTask) {
                        addNewSubtaskNoSave((SubTask) task, ((SubTask) task).getEpicId());
                    } else {
                        addNewTaskNoSave(task);
                    }
                }
                else {
                    break;
                }
            }

            while ((line = reader.readLine()) != null) {
                var idTasks = historyFromString(line);

                for (var item : idTasks){
                    if (isTaskWithIdExists(TypeTask.Task,item)){
                        historyManager.add(getTaskWithIdExists(TypeTask.Task,item));
                    }

                    if (isTaskWithIdExists(TypeTask.EpicTask,item)){
                        historyManager.add(getTaskWithIdExists(TypeTask.EpicTask,item));
                    }

                    if (isTaskWithIdExists(TypeTask.SubTask,item)){
                        historyManager.add(getTaskWithIdExists(TypeTask.EpicTask,item));
                    }
                }
            }
        }
    }

    public boolean isTaskWithIdExists(TypeTask typeTask, String targetId) {
        switch (typeTask){
            case Task:
                for (var obj : getTasks()) {
                    if (obj.getId().equals(targetId)) {
                        return true;
                    }
                }
                break;
            case EpicTask:
                for (var obj : getEpicTask()) {
                    if (obj.getId().equals(targetId)) {
                        return true;
                    }
                }
                break;
            case SubTask:
                for (var obj : getSubtasks()) {
                    if (obj.getId().equals(targetId)) {
                        return true;
                    }
                }
                break;
        }

        return false;
    }

    public Task getTaskWithIdExists(TypeTask typeTask, String targetId) {
        switch (typeTask){
            case Task:
                for (var obj : getTasks()) {
                    if (obj.getId().equals(targetId)) {
                        return obj;
                    }
                }
                break;
            case EpicTask:
                for (var obj : getEpicTask()) {
                    if (obj.getId().equals(targetId)) {
                        return obj;
                    }
                }
                break;
            case SubTask:
                for (var obj : getSubtasks()) {
                    if (obj.getId().equals(targetId)) {
                        return obj;
                    }
                }
                break;
        }
        return null;
    }

    public Task fromString(String value){
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
                task = new Task(name,description,status);
                task.setId(id);
                break;
            case EpicTask:
                task = new EpicTask(name,description);
                task.setId(id);
                break;
            case SubTask:
                SubTask subTask = new SubTask(name,description,Status.NEW);
                subTask.setId(id);
                subTask.setEpicId(epicId);
                task = subTask;
                break;
        }
        return task;
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(getTypeTask(task)).append(",");
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            sb.append(subTask.getEpicId());
        }
        return sb.toString();
    }

    private TypeTask getTypeTask(Task task){
        if (task instanceof EpicTask) {
            return TypeTask.EpicTask;
        } else if (task instanceof SubTask) {
            return TypeTask.SubTask;
        }else {
            return TypeTask.Task;
        }
    }

    static String historyToString(HistoryManager manager){
        var tasks = manager.getTasks();
        var line = "";
        for (var task : tasks){
            line = task.getId() + ", ";
        }
        return line;
    }

    static List<String> historyFromString(String value){
         var line = value.split(",");
         List<String> listID = new ArrayList<>();
         for (var item : line){
             if(item.length()>1) {
                 listID.add(item);
             }
         }
         return listID;
    }

    @Override
    public String addNewTask(Task task) {
        String taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    @Override
    public String addNewEpic(EpicTask epic) {
        String epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public String addNewSubtask(SubTask subtask, String epicID) {
        String subtaskId = super.addNewSubtask(subtask, epicID);
        save();
        return subtaskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(EpicTask epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(String id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(String epicID) {
        super.deleteEpic(epicID);
        save();
    }

    @Override
    public void deleteSubtask(String id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks(String epicID) {
        super.deleteSubtasks(epicID);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void printViewedTask() {
        super.printViewedTask();
        save();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    @Override
    public Task getTask(String id) {
        Task task = super.getTask(id);
        appendIdToFile(id);
        return task;
    }
    @Override
    public SubTask getSubtask(String id) {
        SubTask subTask = super.getSubtask(id);
        appendIdToFile(id);
        return subTask;
    }

    @Override
    public EpicTask getEpic(String id) {
        EpicTask epicTask = super.getEpic(id);
        appendIdToFile(id);
        return epicTask;
    }

    private String addNewTaskNoSave(Task task) {
        return super.addNewTask(task);
    }

    private String addNewEpicNoSave(EpicTask epic) {
        return super.addNewEpic(epic);
    }

    private String addNewSubtaskNoSave(SubTask subtask, String epicID) {
        return super.addNewSubtask(subtask, epicID);
    }

    private void appendIdToFile(String id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(id + ",");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main() throws ManagerLoadException {
        FileBackedTasksManager manager1 = new FileBackedTasksManager("tasks.csv");

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        EpicTask epic1 = new EpicTask("Epic 1", "Description 3");
        SubTask subtask1 = new SubTask("Subtask 1", "Description 4", Status.NEW);

        manager1.addNewTask(task1);
        manager1.addNewTask(task2);
        manager1.addNewEpic(epic1);
        manager1.addNewSubtask(subtask1, epic1.getId());

        manager1.getTask(task1.getId());
        manager1.getTask(task2.getId());
        manager1.getEpic(epic1.getId());


        FileBackedTasksManager manager2 = new FileBackedTasksManager("tasks.csv");
        System.out.println("Количество задач в менеджере историй" + manager2.historyManager.getTasks());
    }
}
