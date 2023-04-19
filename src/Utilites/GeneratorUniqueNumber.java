package Utilites;
import java.util.UUID;
public class GeneratorUniqueNumber {

    //static GeneratorUniqueNumber generatorUniqueNumber = new GeneratorUniqueNumber();

   public String generateUniqueID(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString;
    }
}
