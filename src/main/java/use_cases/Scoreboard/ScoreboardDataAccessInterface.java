package use_cases.Scoreboard;

import entities.User;

import java.util.List;

public interface ScoreboardDataAccessInterface {

    void saveResults(int lobbyPin, List<User> users);

    List<User> loadResults(int lobbyPin);
}
