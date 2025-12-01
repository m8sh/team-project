package use_cases.scoreboard;

import entities.Lobby;

import java.util.List;

public interface ScoreboardDataAccessInterface {
    Lobby getLobby(int pin);
}
