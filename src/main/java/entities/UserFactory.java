package entities;

public class UserFactory {
    public User makeUser(String name, int lobbyPin) {
        return new User(name, lobbyPin);
    }
}
