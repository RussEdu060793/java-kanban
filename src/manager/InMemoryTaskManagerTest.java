package manager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}
