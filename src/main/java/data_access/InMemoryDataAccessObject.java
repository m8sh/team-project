package data_access;

import entities.Lobby;

public class InMemoryDataAccessObject {
    private Lobby lobby;

    public InMemoryDataAccessObject(Lobby lobby) {
        this.lobby = lobby;
        //currently only supports one lobby
    }
    public Lobby getLobby() {
        return lobby;
    }

    public void saveLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobbyByPin(int pin) {
        if (lobby != null && lobby.getPin() == pin) {
            return lobby;
        }
        return null;
    }
}