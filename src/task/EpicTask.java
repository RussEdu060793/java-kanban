package task;
import java.util.ArrayList;

public class EpicTask extends Task {
    ArrayList<String> idSubTasks;

    public EpicTask(String title, String description, ArrayList<String> idSubTasks) {
        super(title, description, Status.NEW);
        this.idSubTasks = idSubTasks;
    }

    public EpicTask(String title, String description) {
        super(title, description, Status.NEW);
        this.idSubTasks = new ArrayList<>();
    }

}