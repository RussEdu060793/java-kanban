package task;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private String epicId;

    public SubTask(String title, String description, Status status) {
        super(title, description, status);
    }

    public SubTask(String title, String description, Status status, int duration, LocalDateTime localDateTime) {
        super(title, description, status, duration, localDateTime);
    }

    public SubTask(String title, String description, String id) {
        super(title, description, Status.NEW);
    }

    public String getEpicId() {
        return epicId;
    }

    public void setEpicId(String epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId='" + epicId + '\'' +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                '}';
    }

    @Override
    public TypeTask getType(){
        return TypeTask.SubTask;
    }
}
