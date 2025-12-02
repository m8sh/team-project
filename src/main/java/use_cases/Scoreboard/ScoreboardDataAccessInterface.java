package use_cases.Scoreboard;

import java.util.List;

import entities.User;

/**
 * Data access interface for saving and loading scoreboard results.
 */
public interface ScoreboardDataAccessInterface {
    /**
     * Load the users and their scores for a lobby.
     *
     * @param lobbyPin The lobby PIN.
     * @param users The users
     */
    void saveResults(int lobbyPin, List<User> users);
    /**
     * Load the users and their scores for a lobby.
     *
     * @param lobbyPin The lobby PIN.
     * @return A list of users for this lobby.
     */

    List<User> loadResults(int lobbyPin);
}
