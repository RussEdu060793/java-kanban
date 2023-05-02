package manager;

import task.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    private final int MAX_LENGTH_HISTORY = 10;

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MAX_LENGTH_HISTORY) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
