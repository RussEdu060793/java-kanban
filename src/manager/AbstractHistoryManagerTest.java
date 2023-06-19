package manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

public abstract class AbstractHistoryManagerTest<T extends HistoryManager> {
    protected T historyManager;

    @BeforeEach
    void setUp() {
        historyManager = createHistoryManager();
    }

    protected abstract T createHistoryManager();

    @Test
    void testAddToHistory_emptyHistory() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);

        //When
        historyManager.add(task);

        //Then
        assertTrue(historyManager.getTasks().contains(task));
    }

    @Test
    void testAddToHistory_duplicateTask() {
        //Given
        Task task = new Task("Task", "Description", Status.NEW);
        historyManager.add(task);

        //When
        historyManager.add(task);

        //Then
        assertEquals(1, historyManager.getTasks().size());
    }

    @Test
    void testRemoveFromHistory_startOfHistory() {
        //Given
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Task task2 = new Task("Task2", "Description", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        //When
        historyManager.remove(task1.getId());

        //Then
        assertFalse(historyManager.getTasks().contains(task1));
        assertTrue(historyManager.getTasks().contains(task2));
    }

    @Test
    void testRemoveFromHistory_middleOfHistory() {
        //Given
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Task task2 = new Task("Task2", "Description", Status.NEW);
        Task task3 = new Task("Task3", "Description", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        //When
        historyManager.remove(task2.getId());

        //Then
        assertTrue(historyManager.getTasks().contains(task1));
        assertFalse(historyManager.getTasks().contains(task2));
        assertTrue(historyManager.getTasks().contains(task3));
    }

    @Test
    void testRemoveFromHistory_endOfHistory() {
        // Arrange
        Task task1 = new Task("Task1", "Description", Status.NEW);
        Task task2 = new Task("Task2", "Description", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        // Act
        historyManager.remove(task2.getId());

        // Assert
        assertTrue(historyManager.getTasks().contains(task1));
        assertFalse(historyManager.getTasks().contains(task2));
    }
}

