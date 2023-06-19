package task;
import utilites.UniqueNumberGenerator;

import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private String id;
    private Status status;
    private int duration; // in minutes
    private LocalDateTime startTime;

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

    public Task(String title, String description, Status status, int duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = UniqueNumberGenerator.generateUniqueID();
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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
        return TypeTask.Task;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }
}
