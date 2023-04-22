package utilites;
import java.util.UUID;

public class GeneratorUniqueNumber {

    static public GeneratorUniqueNumber generatorUniqueNumber = new GeneratorUniqueNumber();

    public String generateUniqueID() {

        return UUID.randomUUID().toString();
    }
}
