package data_access;

import entities.Lobby;
import use_cases.scoreboard.ScoreboardDataAccessInterface;

public class InMemoryDataAccessObject
        implements ScoreboardDataAccessInterface {

    private Lobby lobby;

    public InMemoryDataAccessObject() {
        this.lobby = null;
    }

    // AddQuestionLobbyDataAccessInterface methods


    // ScoreboardDataAccessInterface method

    @Override
    public Lobby getLobby(int pin) {
        if (lobby != null && lobby.getPin() == pin) {
            return lobby;
        }
        return null;
    }
}