package exceptions;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
        super();
    }

    public ManagerLoadException(String message, Exception e) {
        super(message, e);
    }

    public ManagerLoadException(String message) {
        super(message);
    }
}
