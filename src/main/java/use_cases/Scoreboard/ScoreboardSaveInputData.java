package use_cases.Scoreboard;

import java.util.List;

import entities.User;

public class ScoreboardSaveInputData {

    private final int lobbyPin;
    private final List<User> users;

    public ScoreboardSaveInputData(int lobbyPin, List<User> users) {
        this.lobbyPin = lobbyPin;
        this.users = users;
    }

    public int getLobbyPin() {
        return lobbyPin;
    }

    public List<User> getUsers() {
        return users;
    }
}
