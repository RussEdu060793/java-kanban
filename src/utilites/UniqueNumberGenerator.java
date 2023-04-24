package utilites;
import java.util.UUID;

public class UniqueNumberGenerator {
    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}
