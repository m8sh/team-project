package use_cases.addQuestion;

import entities.Lobby;

public interface AddQuestionLobbyDataAccessInterface {
    Lobby getLobby();
    void saveLobby(Lobby lobby);

}
// add question interactor needs to interact with a lobby, to put the questions int
// Needs to interact with question entities
//So Data Access interface
// Interactor needs to