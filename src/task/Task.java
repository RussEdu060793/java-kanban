package task;
import utilites.UniqueNumberGenerator;

public class Task {
    private String title;
    private String description;
    private String id;
    private Status status;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.id = UniqueNumberGenerator.generateUniqueID();
        this.status = status;
    }

    public Task(String title, String description,String id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getType(){
        if (this.getClass().equals(Task.class)) {
            return TypeTask.Task;
        } else if (this.getClass().equals(EpicTask.class)) {
            return TypeTask.EpicTask;
        } else if (this.getClass().equals(SubTask.class)) {
            return TypeTask.SubTask;
        }
        return TypeTask.Task;
    }
}
