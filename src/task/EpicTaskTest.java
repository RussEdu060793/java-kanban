package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicTaskTest {

    @Test
    public void testGetStatus_EmptySubtasksList() {
        //Given
        EpicTask epicTask = new EpicTask("Epic", "Description");

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);

        //Then
        Assertions.assertEquals(Status.NEW, taskManager.getEpic(epicTask.getId()).getStatus());
    }

    @Test
    public void testGetStatus_AllSubtasksNew() {
        //Given
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 1", Status.NEW));
        subtasks.add(new SubTask("Subtask 2", "Description 2", Status.NEW));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());

        //Then
        Assertions.assertEquals(Status.NEW, taskManager.getEpic(epicTask.getId()).getStatus());
    }

    @Test
    public void testGetStatus_AllSubtasksDone() {
        //Given
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 1", Status.DONE));
        subtasks.add(new SubTask("Subtask 2", "Description 2", Status.DONE));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());
        taskManager.addNewSubtask(subtasks.get(1), epicTask.getId());

        //Then
        Assertions.assertEquals(Status.DONE, taskManager.getEpic(epicTask.getId()).getStatus());
    }

    @Test
    public void testGetStatus_MixedSubtaskStatuses() {
        //Given
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 1", Status.NEW));
        subtasks.add(new SubTask("Subtask 2", "Description 2", Status.DONE));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());
        taskManager.addNewSubtask(subtasks.get(1), epicTask.getId());

        //Then
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epicTask.getId()).getStatus());
    }

    @Test
    public void testGetStatus_AllSubtasksInProgress() {
        //Given
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 1", Status.IN_PROGRESS));
        subtasks.add(new SubTask("Subtask 2", "Description 2", Status.IN_PROGRESS));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());
        taskManager.addNewSubtask(subtasks.get(1), epicTask.getId());

        //Then
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epicTask.getId()).getStatus());
    }
    @Test
    public void testDuration_GetEpicDuration(){
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 4", Status.NEW, 60, LocalDateTime.of(2023,8,06,12,00)));
        subtasks.add(new SubTask("Subtask 2", "Description 4", Status.NEW, 60, LocalDateTime.of(2023,8,06,13,00)));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());
        taskManager.addNewSubtask(subtasks.get(1), epicTask.getId());

        //Then
        Assertions.assertTrue(taskManager.getEpic(epicTask.getId()).getDuration(taskManager) == 120);
    }

    @Test
    public void testEndTime_GetEpicEndTime(){
        EpicTask epicTask = new EpicTask("Epic", "Description");
        List<SubTask> subtasks = new ArrayList<>();
        subtasks.add(new SubTask("Subtask 1", "Description 4", Status.NEW, 60, LocalDateTime.of(2023,8,06,12,00)));
        subtasks.add(new SubTask("Subtask 2", "Description 4", Status.NEW, 60, LocalDateTime.of(2023,8,06,13,00)));

        //When
        var taskManager = new InMemoryTaskManager();
        taskManager.addNewEpic(epicTask);
        taskManager.addNewSubtask(subtasks.get(0), epicTask.getId());
        taskManager.addNewSubtask(subtasks.get(1), epicTask.getId());

        //Then
        Assertions.assertTrue(taskManager.getEpic(epicTask.getId()).getEndTime(taskManager).isEqual(LocalDateTime.of(2023,8,06,14,00)));
    }
}