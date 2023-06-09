package exceptions;

import java.io.IOException;

public class LoadFileExeption extends IOException {

   public LoadFileExeption(String message) {
        super(message);
    }

    public LoadFileExeption() {
        super();
    }

    public LoadFileExeption(String message, Throwable cause) {
        super(message,cause);
    }

}
