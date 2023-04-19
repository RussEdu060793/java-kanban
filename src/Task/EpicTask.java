package Task;

import Utilites.GeneratorUniqueNumber;

import java.util.ArrayList;

public class EpicTask extends Task {

    ArrayList<SubTask> subTasks;
    public String id;
    private GeneratorUniqueNumber generatorID;
    public EpicTask(String title, String description, ArrayList<SubTask> subTasks) {

        super(title, description, Status.NEW);
        this.generatorID = new GeneratorUniqueNumber();
        this.id = generatorID.generateUniqueID();
        this.subTasks = subTasks;
        updateEpicStatus();
    }

    public EpicTask(String title, String description) {

        super(title, description, Status.NEW);
        this.generatorID = new GeneratorUniqueNumber();
        this.id = generatorID.generateUniqueID();
        this.subTasks = new ArrayList<>();
        updateEpicStatus();
    }

    public EpicTask(String title, String description, String id) {

        super(title, description, Status.NEW);
        this.id = id;
        this.subTasks = new ArrayList<>();
        updateEpicStatus();
    }

    public void updateEpicStatus(){

        if(isAllSubTasksDone()) {
            setStatus(Status.DONE);
        }else {
            if (isHasProgressStatus()){
                setStatus(Status.IN_PROGRESS);
            }else {
                setStatus(Status.NEW);
            }
        }
    }
    public boolean isAllSubTasksDone() {

        if (subTasks.isEmpty()) {
            return false;
        }

        for (SubTask subTask : this.subTasks) {
            if (subTask.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    private boolean isHasProgressStatus(){

        if (subTasks.isEmpty()) {
            return false;
        }

        for (SubTask subTask : this.subTasks) {
            if (subTask.getStatus() == Status.IN_PROGRESS || subTask.getStatus() == Status.DONE) {
                return true;
            }
        }

        return false;
    }
    public void addSubTask(SubTask subTask){

        subTasks.add(subTask);
        updateEpicStatus();
    }

    public ArrayList<String> printTitleSubTask(){

        ArrayList<String> arrayOfTitles = new ArrayList<>();

        for (var item : subTasks){
            arrayOfTitles.add(item.getTitle());
        }

        return arrayOfTitles;
    }

    public void printSubTask(){

        System.out.println(subTasks.toString());
    }

    public ArrayList<SubTask> getSubTasks(){

        return this.subTasks;
    }
}