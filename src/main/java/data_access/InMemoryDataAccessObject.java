/*package data_access;

import entities.Lobby;
import use_cases.AddQuestion.AddQuestionLobbyDataAccessInterface;

public class InMemoryDataAccessObject implements AddQuestionLobbyDataAccessInterface {
    private Lobby lobby;

    public InMemoryDataAccessObject(Lobby lobby) {
        this.lobby = lobby;
        //currently only supports one lobby
    }
    @Override
    public Lobby getLobby() {
        return lobby;
    }
    @Override
    public void saveLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}

 */