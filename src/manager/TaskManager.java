package manager;
import task.*;
import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    List<SubTask> getSubtasks();

    List<EpicTask> getEpicTask();

    List<SubTask> getEpicSubtasks(String epicId);

    Task getTask(String id);

    SubTask getSubtask(String id);

    EpicTask getEpic(String id);

    String addNewTask(Task task);

    String addNewEpic(EpicTask epic);

    String addNewSubtask(SubTask subtask, String epicID);

    void updateTask(Task task);

    void updateEpic(EpicTask epic);

    void updateSubtask(SubTask subtask);

    void deleteTask(String id);

    void deleteEpic(String epicID);

    void deleteSubtask(String id);

    void deleteTasks();

    void deleteSubtasks(String epicID);

    void deleteEpics();

    void printViewedTask();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks(TypeTask type);
}
