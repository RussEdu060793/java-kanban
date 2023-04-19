package Task;
import Utilites.GeneratorUniqueNumber;

public class SubTask extends Task {

    public EpicTask epicTask;
    public String id;
    private GeneratorUniqueNumber generatorID = new GeneratorUniqueNumber();

    public SubTask(String title, String description, Status status, EpicTask epicTask) {
        super(title, description, status);
        this.epicTask = epicTask;
        this.id = generatorID.generateUniqueID();
    }

    public SubTask(String title, String description, String id, EpicTask epicTask) {
        super(title, description, Status.NEW);
        this.epicTask = epicTask;
        this.id = id;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id='" + id + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicTask=" + epicTask.id +
                '}';
    }
}
