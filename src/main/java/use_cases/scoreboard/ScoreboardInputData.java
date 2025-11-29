package use_cases.scoreboard;

public class ScoreboardInputData {
    private final int lobbyPin;

    public ScoreboardInputData(int lobbyPin) {
        this.lobbyPin = lobbyPin;
    }

    public int getLobbyPin() {
        return lobbyPin;
    }
}
