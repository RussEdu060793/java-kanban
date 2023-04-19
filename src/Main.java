import Manager.Manager;
import Task.EpicTask;
import Task.SubTask;
import Task.Task;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        var scanner = new  Scanner(System.in);
        int userInput = 0;

        while (true){
            System.out.println("Выберите пункт меню");
            printMenu();
            userInput = ServiceControler.instance.isValidInput(scanner,1,3);

            switch (userInput){
                case 0:
                    return;
                case 1:
                   ServiceControler.instance.createTask(scanner);
                   break;
                case 2:
                    ServiceControler.instance.openTask(scanner);
                    break;
                case 3:
                    ServiceControler.instance.deleteTask(scanner);
                    break;
            }
        }
    }
   public static void printMenu(){
        System.out.println("0: Выход");
        System.out.println("1: Создать задачу");
        System.out.println("2: Зайти в задачу");
        System.out.println("3: Удалить задачу");
    }
}
