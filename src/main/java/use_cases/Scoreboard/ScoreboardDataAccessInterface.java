package use_cases.Scoreboard;

import entities.Lobby;

public interface ScoreboardDataAccessInterface {
    Lobby getLobby(int pin);
}
