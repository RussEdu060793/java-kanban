package exepctions;

public class ManagerLoadException extends Exception {
    public ManagerLoadException() {
        super();
    }

    public ManagerLoadException(String message, Exception e){
        super(message,e);
    }

    public ManagerLoadException(String message) {
        super(message);
    }
}
