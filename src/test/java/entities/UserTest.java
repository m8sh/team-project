package entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserInitialization() {
        User user = new User("Alice", 1234);

        // Check that fields are initialized correctly
        assertEquals("Alice", user.getName());
        assertEquals(0, user.getScore()); // default score
        assertEquals(1234, user.getLobbyPin());
    }
    @Test
    void testScoreCanBeRetrieved() {
        User user = new User("Bob", 5678);

        // Initial score should be 0
        assertEquals(0, user.getScore());
    }

    @Test
    void testNameGetter() {
        User user = new User("Charlie", 9012);

        assertEquals("Charlie", user.getName());
    }

    @Test
    void testLobbyPinGetter() {
        User user = new User("Dana", 3456);

        assertEquals(3456, user.getLobbyPin());
    }
    @Test
    void testUserFactory() {
        UserFactory factory = new UserFactory();
        User B = factory.makeUser("Bob", 5678);
        assertEquals("Bob", B.getName());
        assertEquals(5678, B.getLobbyPin());

    }


}