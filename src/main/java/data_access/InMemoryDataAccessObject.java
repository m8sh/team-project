package data_access;

import entities.Lobby;
import use_cases.addQuestion.AddQuestionLobbyDataAccessInterface;
import use_cases.NextQuestion.NextQuestionLobbyDataAccessInterface;
import use_cases.StartScreen.StartScreenLobbyDataAccessInterface;
import use_cases.scoreboard.ScoreboardDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDataAccessObject implements AddQuestionLobbyDataAccessInterface,
        NextQuestionLobbyDataAccessInterface,
        ScoreboardDataAccessInterface, StartScreenLobbyDataAccessInterface {

    private Lobby lobby;

    public InMemoryDataAccessObject() {
        this.lobby = null;
        //currently only supports one lobby



    }
    @Override
    public Lobby getLobby() {
        return lobby;
    }

    // AddQuestionLobbyDataAccessInterface methods


    // ScoreboardDataAccessInterface method

    @Override
    public Lobby getLobby(int pin) {
        if (lobby != null && lobby.getPin() == pin) {
            return lobby;
        }
        return null;
    }

    public void saveLobby(Lobby lobby) {
//        System.out.println("DAO: saving lobby " + lobby.getPin()+ " with " + lobby.getQuestions().size() + " questions");
        this.lobby = lobby;
    }


}


//private Map<Integer, Lobby> PinToLobby;
//public LobbyManager() {
//    PinToLobby = new HashMap<Integer, Lobby>();
//}
//public void addLobby(Lobby lobby) {
//    PinToLobby.put(lobby.getPin(), lobby);
//}
//public Lobby getLobby(int pin) {
//    return PinToLobby.get(pin);
//}
//public void removeLobby(int pin) {
//    PinToLobby.remove(pin);
//}