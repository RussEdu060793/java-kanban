package manager;
import task.*;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);
    void remove(String id);
    ArrayList<Task> getTasks();
}
