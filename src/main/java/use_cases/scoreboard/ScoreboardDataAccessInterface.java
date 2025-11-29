package use_cases.scoreboard;

import entities.Lobby;
public interface ScoreboardDataAccessInterface {
    Lobby getLobby(int pin);
}
