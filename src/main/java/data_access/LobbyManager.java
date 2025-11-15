package data_access;

import entities.Lobby;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager {
    private Map<Integer, Lobby> PinToLobby;
    public LobbyManager() {
        PinToLobby = new HashMap<Integer, Lobby>();
    }
    public void addLobby(Lobby lobby) {
        PinToLobby.put(lobby.getPin(), lobby);
    }
    public Lobby getLobby(int pin) {
        return PinToLobby.get(pin);
    }
    public void removeLobby(int pin) {
        PinToLobby.remove(pin);
    }


}
