package manager;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest extends AbstractHistoryManagerTest<HistoryManager> {

    @Override
    protected HistoryManager createHistoryManager() {
        return new InMemoryHistoryManager();
    }
}