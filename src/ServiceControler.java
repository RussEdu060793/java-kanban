import Manager.Manager;
import Task.EpicTask;
import Task.SubTask;
import Task.Task;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ServiceControler {

    public static ServiceControler instance = new ServiceControler();

    public int isValidInput(Scanner scanner, int from, int to) {
        try {
            int input = scanner.nextInt();
            if (input >= from && input <= to) {
                return input;
            } else {
                System.out.println("Вы ввели неизвестное значение");
                return 0;
            }
        } catch (InputMismatchException e) {
            System.out.println("Вы ввели неизвестное значение");
            return 0;
        }
    }

    public String isValidSelection(Scanner scanner, String yes, String no) {
        try {
            String input = scanner.next();
            if (input.equals(yes) || input.equals(no)) {
                return input;
            } else {
                return "";
            }
        } catch (InputMismatchException e) {
            System.out.println("Вы ввели неизвестное значение");
            return "";
        }
    }

    public void createTask(Scanner scanner) {
        System.out.println("Напишите имя вашей задачи");
        String nameTask = scanner.next();
        System.out.println("Напишите описание для вашей задачи");
        String descTask = scanner.next();
        System.out.println("Добавить в подзадачи? Y/N");
        var isEpic = isValidSelection(scanner, "Y", "N");
        if (isEpic.equals("Y")) {
            ///проверяем что есть задачи или epic
            if (Manager.shared.filterNotSubTask().size() > 0) {
                //выводим список задач которые не являются подзадачами
                System.out.println("Выберите задачу в которой создать подзадачу");
                Manager.shared.printListOfTask(Manager.shared.filterNotSubTask());
                var indexTask = isValidInput(scanner, 1, Manager.shared.getCountListOfTaskAndEpic());

                if (indexTask == 0) {
                    return;
                }

                var idTask = Manager.shared.getTaskById(Manager.shared
                        .filterNotSubTask().get((indexTask - 1)).getId()).getId();

                if (!(Manager.shared.getTaskById(idTask) instanceof EpicTask)) {
                    var epicTask = Manager.shared.convertingTaskToEpic(Manager.shared.getTaskById(idTask));
                    Manager.shared.addingTask(epicTask);
                    SubTask subTask = new SubTask(nameTask, descTask, Task.Status.NEW, epicTask);
                    Manager.shared.addingTask(subTask);
                } else if (Manager.shared.getTaskById(idTask) instanceof EpicTask) {
                    var epicTask = Manager.shared.getEpicTaskById(idTask);
                    SubTask subTask = new SubTask(nameTask, descTask, Task.Status.NEW, epicTask);
                    Manager.shared.addingTask(subTask);
                }
            } else {
                System.out.println("Еще нет задач чтобы создать подзадачу");
            }
        } else if (isEpic.equals("N")) {
            Task task = new Task(nameTask, descTask, Task.Status.NEW);
            Manager.shared.addingTask(task);
            System.out.println("Задача добавлена");
        } else {
            System.out.println("Ответ не распознан");
        }
    }

    public void deleteTask(Scanner scanner) {
        if (Manager.shared.filterNotSubTask().size() == 0) {
            System.out.println("Нет задач которые можно удалить");
            return;
        }
        System.out.println("Выберите номер какую задачу удалить");
        Manager.shared.printListOfTask(Manager.shared.filterNotSubTask());
        var indexTask = isValidInput(scanner, 1, Manager.shared.filterNotSubTask().size());
        if (indexTask == 0) {
            System.out.println("Нельзя удалить несуществующий объект");
            return;
        }
        var idTask = Manager.shared.getTaskById(Manager.shared.filterNotSubTask().get((indexTask - 1)).getId()).getId();
        Manager.shared.deleteTaskByID(idTask);
    }

    public void openTask(Scanner scanner) {
        if (Manager.shared.filterNotSubTask().size() == 0) {
            System.out.println("У вас нет задач");
            return;
        }

        System.out.println("Выберите номер какую задачу открыть");
        Manager.shared.printListOfTask(Manager.shared.filterNotSubTask());

        var indexTask = isValidInput(scanner,1,Manager.shared.filterNotSubTask().size());
        if (indexTask == 0){
            System.out.println("Нельзя выбрать несуществующую задачу");
            return;
        }
        var selectedTask = Manager.shared.filterNotSubTask().get(indexTask - 1);

        System.out.println("Вы открыли задачу");
        System.out.println("Название задачи: " + selectedTask.getTitle());
        System.out.println("    Описание задачи: " + selectedTask.getDescription());
        System.out.println("    Статус задачи: " + selectedTask.getStatus());

        if (selectedTask instanceof EpicTask) {
            var temp = (EpicTask) selectedTask;
            if (temp.getSubTasks().size() > 0) {
                System.out.println("У задачи есть подзадачи");
                var index = 0;
                for (var task : temp.getSubTasks()) {
                    System.out.println("\nНазвание подзадачи: " + (index + 1) + ": " + task.getTitle());
                    System.out.println("    Описание: " + task.getDescription());
                    System.out.println("    Статус: " + task.getStatus());
                    index++;
                }
                System.out.println("Хотите изменить статус подзадачи? Y/N");
                var changeStatus = isValidSelection(scanner, "Y", "N");
                if (changeStatus.equals("Y")) {
                    System.out.println("Выберите номер подзадачи в которой будем менять статус");
                    var selectInedexSubTask = isValidInput(scanner, 1, temp.getSubTasks().size());
                    var idSubTask = temp.getSubTasks().get(selectInedexSubTask - 1).getId();

                    System.out.println("Как на что изменить статус");
                    System.out.println("Доступны статусы");
                    System.out.println("1. NEW");
                    System.out.println("2. IN PROGRESS");
                    System.out.println("3. DONE");

                    var inputStatus = isValidInput(scanner,1,3);
                    Task.Status status = Task.Status.NEW;
                    switch (inputStatus) {
                        case 1:
                            status = Task.Status.NEW;
                            break;
                        case 2:
                            status = Task.Status.IN_PROGRESS;
                            break;
                        case 3:
                            status = Task.Status.DONE;
                    }
                    Manager.shared.changeSubTaskStatus(idSubTask, status);
                } else if (changeStatus.equals("N")) {
                    return;
                } else {
                    System.out.println("Введена неправильная команда, попробуйте еще раз");
                    return;
                }
            }
        } else {
            System.out.println("Хотите изменить статус ? Y/N");
            var changeStatus = isValidSelection(scanner,"Y","N");
            if (changeStatus.equals("Y")) {

                System.out.println("Как изменить статус");
                System.out.println("Доступные статусы");
                System.out.println("1. NEW");
                System.out.println("2. IN PROGRESS");
                System.out.println("3. DONE");

                var inputStatus = isValidInput(scanner,1,3);
                Task.Status status = Task.Status.NEW;
                switch (inputStatus) {
                    case 1:
                        selectedTask.setStatus(Task.Status.NEW);
                        break;
                    case 2:
                        selectedTask.setStatus(Task.Status.IN_PROGRESS);
                        break;
                    case 3:
                        selectedTask.setStatus(Task.Status.DONE);
                }
            } else if (changeStatus.equals("N")) {
                return;
            } else {
                System.out.println("Введена неправильная команда, попробуйте еще раз");
                return;
            }
        }
    }
}
