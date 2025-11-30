package use_cases.StartScreen;

import entities.Lobby;

public interface StartScreenLobbyDataAccessInterface {

    Lobby getLobby(int pin);
    void saveLobby(Lobby lobby);

}
