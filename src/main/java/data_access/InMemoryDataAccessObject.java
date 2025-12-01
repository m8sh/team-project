package data_access;

import entities.Lobby;
import entities.User;

// ✅ Correct imports
import use_cases.AddQuestion.AddQuestionLobbyDataAccessInterface;

import use_cases.NextQuestion.NextQuestionLobbyDataAccessInterface;
import use_cases.StartScreen.StartScreenLobbyDataAccessInterface;
import use_cases.Scoreboard.ScoreboardDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDataAccessObject implements
        AddQuestionLobbyDataAccessInterface,
        NextQuestionLobbyDataAccessInterface,
        ScoreboardDataAccessInterface,
        StartScreenLobbyDataAccessInterface {

    private Lobby lobby;
    private final List<User> scoreboardUsers = new ArrayList<>();

    public InMemoryDataAccessObject() {
        this.lobby = null;
    }

    // -------- AddQuestionLobbyDataAccessInterface --------
    @Override
    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public void saveLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    // -------- StartScreenLobbyDataAccessInterface --------
    @Override
    public Lobby getLobby(int pin) {
        if (lobby != null && lobby.getPin() == pin) {
            return lobby;
        }
        return null;
    }

    // -------- ScoreboardDataAccessInterface --------
    @Override
    public void saveResults(int lobbyPin, List<User> users) {
        scoreboardUsers.clear();
        scoreboardUsers.addAll(users);
    }

    @Override
    public List<User> loadResults(int lobbyPin) {
        return new ArrayList<>(scoreboardUsers);
    }
}
