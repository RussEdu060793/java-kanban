package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import manager.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @AfterEach
    void tearDown() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.csv"))) {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddNewTask_DefaultBehavior() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);

        //When
        String taskId = taskManager.addNewTask(task);
        Task retrievedTask = taskManager.getTask(taskId);

        //Then
        Assertions.assertNotNull(retrievedTask);
        Assertions.assertEquals(task.getTitle(), retrievedTask.getTitle());
        Assertions.assertEquals(task.getDescription(), retrievedTask.getDescription());
        Assertions.assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    void testAddNewTask_EmptyTaskList() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.deleteTasks();

        //When
        String taskId = taskManager.addNewTask(task);
        Task retrievedTask = taskManager.getTask(taskId);

        //Then
        Assertions.assertNotNull(retrievedTask);
        Assertions.assertEquals(task.getTitle(), retrievedTask.getTitle());
        Assertions.assertEquals(task.getDescription(), retrievedTask.getDescription());
        Assertions.assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    void testAddNewTask_InvalidTaskId() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);

        //When
        String invalidTaskId = "";
        task.setId(invalidTaskId);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addNewTask(task);
        });
    }

    @Test
    void testGetTask_DefaultBehavior() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);
        String taskId = taskManager.addNewTask(task);

        //When
        Task retrievedTask = taskManager.getTask(taskId);

        //Then
        Assertions.assertNotNull(retrievedTask);
        Assertions.assertEquals(task.getTitle(), retrievedTask.getTitle());
        Assertions.assertEquals(task.getDescription(), retrievedTask.getDescription());
        Assertions.assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    void testGetTask_EmptyTaskList() {
        //Given

        //When
        taskManager.deleteTasks();

        //Then
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void testGetTask_InvalidTaskId() {
        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.getTask("invalid_task_id");
        });
    }

    @Test
    void testAddNewEpic_DefaultBehavior() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");

        //When
        String epicId = taskManager.addNewEpic(epic);
        EpicTask retrievedEpic = taskManager.getEpic(epicId);

        //Then
        Assertions.assertNotNull(retrievedEpic);
        Assertions.assertEquals(epic.getTitle(), retrievedEpic.getTitle());
        Assertions.assertEquals(epic.getDescription(), retrievedEpic.getDescription());
    }

    @Test
    void testAddNewEpic_EmptyTaskList() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");

        //When
        taskManager.deleteEpics();
        String epicId = taskManager.addNewEpic(epic);
        EpicTask retrievedEpic = taskManager.getEpic(epicId);

        //Then
        Assertions.assertNotNull(retrievedEpic);
        Assertions.assertEquals(epic.getTitle(), retrievedEpic.getTitle());
        Assertions.assertEquals(epic.getDescription(), retrievedEpic.getDescription());
    }

    @Test
    void testAddNewEpic_InvalidEpicId() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String invalidEpicId = "";

        //When
        epic.setId(invalidEpicId);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addNewEpic(epic);
        });
    }

    @Test
    void testGetEpic_DefaultBehavior() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);

        //When
        EpicTask retrievedEpic = taskManager.getEpic(epicId);

        //Then
        Assertions.assertNotNull(retrievedEpic);
        Assertions.assertEquals(epic.getTitle(), retrievedEpic.getTitle());
        Assertions.assertEquals(epic.getDescription(), retrievedEpic.getDescription());
    }

    @Test
    void testGetEpic_EmptyEpicList() {
        //Given
        taskManager.deleteEpics();

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.getEpic("invalid_epic_id");
        });
    }

    @Test
    void testGetEpic_InvalidEpicId() {
        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.getEpic("invalid_epic_id");
        });
    }

    @Test
    void testAddNewSubtask_DefaultBehavior() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);
        SubTask subtask = new SubTask("Subtask", "Description", Status.NEW);
        String subtaskId = taskManager.addNewSubtask(subtask, epicId);

        //When
        SubTask retrievedSubtask = taskManager.getSubtask(subtaskId);

        //Then
        Assertions.assertNotNull(retrievedSubtask);
        Assertions.assertEquals(subtask.getTitle(), retrievedSubtask.getTitle());
        Assertions.assertEquals(subtask.getDescription(), retrievedSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), retrievedSubtask.getStatus());
        Assertions.assertEquals(epicId, retrievedSubtask.getEpicId());
    }

    @Test
    void testAddNewSubtask_EmptyTaskList() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);
        SubTask subtask = new SubTask("Subtask", "Description", Status.NEW);
        taskManager.deleteSubtasks(epicId);

        //When
        String subtaskId = taskManager.addNewSubtask(subtask, epicId);
        SubTask retrievedSubtask = taskManager.getSubtask(subtaskId);

        //Then
        Assertions.assertNotNull(retrievedSubtask);
        Assertions.assertEquals(subtask.getTitle(), retrievedSubtask.getTitle());
        Assertions.assertEquals(subtask.getDescription(), retrievedSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), retrievedSubtask.getStatus());
        Assertions.assertEquals(epicId, retrievedSubtask.getEpicId());
    }

    @Test
    void testAddNewSubtask_InvalidSubtaskId() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);
        SubTask subtask = new SubTask("Subtask", "Description", Status.NEW);
        String invalidSubtaskId = "";

        //When
        subtask.setId(invalidSubtaskId);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addNewSubtask(subtask, epicId);
        });
    }

    @Test
    void testGetSubtask_DefaultBehavior() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);
        SubTask subtask = new SubTask("Subtask", "Description", Status.NEW);
        String subtaskId = taskManager.addNewSubtask(subtask, epicId);

        //When
        SubTask retrievedSubtask = taskManager.getSubtask(subtaskId);

        //Then
        Assertions.assertNotNull(retrievedSubtask);
        Assertions.assertEquals(subtask.getTitle(), retrievedSubtask.getTitle());
        Assertions.assertEquals(subtask.getDescription(), retrievedSubtask.getDescription());
        Assertions.assertEquals(subtask.getStatus(), retrievedSubtask.getStatus());
        Assertions.assertEquals(epicId, retrievedSubtask.getEpicId());
    }

    @Test
    void testGetSubtask_EmptySubtaskList() {
        //Given
        EpicTask epic = new EpicTask("Epic", "Description");
        String epicId = taskManager.addNewEpic(epic);

        //When
        taskManager.deleteSubtasks(epicId);

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.getSubtask("invalid_subtask_id");
        });
    }

    @Test
    void testGetSubtask_InvalidSubtaskId() {
        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            taskManager.getSubtask("invalid_subtask_id");
        });
    }

}
