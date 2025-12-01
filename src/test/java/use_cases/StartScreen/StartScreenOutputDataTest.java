package use_cases.StartScreen;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StartScreenOutputDataTest {
    @Test
    void allFieldsNotNull() {
        int pin = 123;
        String name = "bob";
        boolean createdSession = true;

        StartScreenOutputData startScreenOutputData = new StartScreenOutputData(pin, name, createdSession);

        assertEquals(pin, startScreenOutputData.getLobbyPin());
        assertEquals(name, startScreenOutputData.getUsername());
        assertTrue(startScreenOutputData.isCreatedSession());
    }

    @Test
    void nullUserSessionBad() {
        int pin = 123;
        String name = null;
        boolean createdSession = false;

        StartScreenOutputData data = new StartScreenOutputData(pin, name, createdSession);

        assertEquals(pin, data.getLobbyPin());
        assertNull(data.getUsername());
        assertFalse(data.isCreatedSession());
    }
}
