import java.util.HashMap;
import java.util.Map;

public class QuestionBank {

    // Class-level field (mutable, static)
    public static Map<String, String[]> questionBank = new HashMap<>();

    static {
        questionBank.put("Capital of Spain?", new String[]{"Madrid", "Barcelona", "Valencia", "Seville", "Madrid"});
        questionBank.put("Capital of France?", new String[]{"Paris", "Lyon", "Marseille", "Nice", "Paris"});
        questionBank.put("Capital of Italy?", new String[]{"Rome", "Milan", "Naples", "Turin", "Rome"});
    }

}