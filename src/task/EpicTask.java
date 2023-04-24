package task;
import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<String> idSubTasks;

    public EpicTask(String title, String description, ArrayList<String> idSubTasks) {
        super(title, description, Status.NEW);
        this.idSubTasks = idSubTasks;
    }

    public EpicTask(String title, String description) {
        super(title, description, Status.NEW);
        this.idSubTasks = new ArrayList<>();
    }

    public ArrayList<String> getidSubTasks(){
        return idSubTasks;
    }

    public void addIDSubTask(String link){
        idSubTasks.add(link);
    }

    public void removeSubtask(String subtaskId) {
        idSubTasks.remove(idSubTasks.indexOf(subtaskId));
    }
}