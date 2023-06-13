package manager;

import exceptions.LoadFileExeption;
import utilites.CSVTaskFormatter;
import exceptions.ManagerLoadException;
import task.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String filePath;

    public FileBackedTasksManager(String filePath) {
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
                writer.write(CSVTaskFormatter.taskToString(task));
                writer.newLine();
            }
            for (EpicTask epic : getEpicTask()) {
                writer.write(CSVTaskFormatter.taskToString(epic));
                writer.newLine();
            }
            for (SubTask subTask : getSubtasks()) {
                writer.write(CSVTaskFormatter.taskToString(subTask));
                writer.newLine();
            }

            writer.newLine();
            saveHistory();
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка в сохранении данных");
        }
    }

    private void loadFromFile(String filePath) throws LoadFileExeption {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1) {
                    Task task = CSVTaskFormatter.fromString(line);
                    if (task.getType() == TypeTask.EpicTask) {
                        addNewEpicNoSave((EpicTask) task);
                    } else if (task.getType() == TypeTask.SubTask) {
                        addNewSubtaskNoSave((SubTask) task, ((SubTask) task).getEpicId());
                    } else {
                        addNewTaskNoSave(task);
                    }
                } else {
                    break;
                }
            }

            while ((line = reader.readLine()) != null) {
                var idTasks = CSVTaskFormatter.historyFromString(line);

                for (var item : idTasks) {
                    if (isTaskWithIdExists(TypeTask.Task, item)) {
                        historyManager.add(getTaskWithIdExists(TypeTask.Task, item));
                    }

                    if (isTaskWithIdExists(TypeTask.EpicTask, item)) {
                        historyManager.add(getTaskWithIdExists(TypeTask.EpicTask, item));
                    }

                    if (isTaskWithIdExists(TypeTask.SubTask, item)) {
                        historyManager.add(getTaskWithIdExists(TypeTask.EpicTask, item));
                    }
                }
            }
        } catch (IOException e) {
            throw new LoadFileExeption("Ошибка загрузки файла");
        }
    }

    private boolean isTaskWithIdExists(TypeTask typeTask, String targetId) {
        switch (typeTask) {
            case Task:
                if (tasks.containsKey(targetId)) {
                    return true;
                }
                break;
            case EpicTask:
                if (epicTasks.containsKey(targetId)) {
                    return true;
                }
                break;
            case SubTask:
                if (subTasks.containsKey(targetId)) {
                    return true;
                }
                break;
        }

        return false;
    }

    private Task getTaskWithIdExists(TypeTask typeTask, String targetId) {
        switch (typeTask) {
            case Task:
                if (tasks.containsKey(targetId)) {
                    return tasks.get(targetId);
                }
                break;
            case EpicTask:
                if (epicTasks.containsKey(targetId)) {
                    return epicTasks.get(targetId);
                }
                break;
            case SubTask:
                if (subTasks.containsKey(targetId)) {
                    return subTasks.get(targetId);
                }
                break;
        }
        return null;
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
            throw new ManagerLoadException("Ошибка добавления в файл");
        }
    }

    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Task task : historyManager.getTasks()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка добавления в файл");
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
