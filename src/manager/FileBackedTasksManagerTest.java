package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Status;
import task.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final String TEST_FILE_PATH = "test_tasks.txt";
    private FileBackedTasksManager taskManager;
    protected FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager("tasks.csv");
    }

    @Test
    void testSaveAndLoadTasks_emptyTasks() {
        // Given
        taskManager = new FileBackedTasksManager("tasks.csv");
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Task task2 = new Task("Task2", "Description", Status.NEW);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        //When
        var taskManager1 = new FileBackedTasksManager("tasks.csv");

        //Then
        assertTrue(taskManager1.getTask(task1.getId()).getId().equals(task1.getId()));
        assertTrue(taskManager1.getTask(task2.getId()).getId().equals(task2.getId()));
    }

    @Test
    void testSaveAndLoadTasks_emptyEpic() {
        // Given
        taskManager = new FileBackedTasksManager("tasks.csv");
        EpicTask epicTask = new EpicTask("Epic1", "Description");

        //When
        taskManager.addNewEpic(epicTask);
        var taskManager1 = new FileBackedTasksManager("tasks.csv");

        //Then
        assertTrue(taskManager1.getEpic(epicTask.getId()).getId().equals(epicTask.getId()));
    }

    @Test
    void testSaveAndLoadTasks_emptyHistory() {
        //Given
        taskManager = new FileBackedTasksManager("tasks.csv");
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Task task2 = new Task("Task2", "Description", Status.NEW);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        HistoryManager historyManager = taskManager.historyManager;
        historyManager.add(task1);
        historyManager.add(task2);

        //When
        var taskManager1 = new FileBackedTasksManager("tasks.csv");

        //Then
        assertTrue(taskManager1.getTasks().stream().filter(t -> t.getId().equals(task1.getId())).findFirst().isPresent());
        assertTrue(taskManager1.getTasks().stream().filter(t -> t.getId().equals(task2.getId())).findFirst().isPresent());
        assertTrue(taskManager1.getHistory().isEmpty());
    }
}
