package use_cases.NextQuestion;

import entities.Lobby;

public interface NextQuestionLobbyDataAccessInterface {
    Lobby getLobby();
    void saveLobby(Lobby lobby);
}