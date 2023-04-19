package Task;
import Utilites.GeneratorUniqueNumber;

public class Task {
    private String title;
    private String description;
    private String id;
    private GeneratorUniqueNumber generatorID = new GeneratorUniqueNumber();
    private Status status;

    public enum Status {
        NEW, IN_PROGRESS, DONE
    }

    public Task(String title, String description, Status status) {

        this.title = title;
        this.description = description;
        this.id = generatorID.generateUniqueID();
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

    public void setId() {

        this.id = generatorID.generateUniqueID();
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }
}