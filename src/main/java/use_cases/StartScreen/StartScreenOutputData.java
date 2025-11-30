package use_cases.StartScreen;

public class StartScreenOutputData {

    private final int lobbyPin;
    private final String username;
    private final boolean createdSession;

    public StartScreenOutputData(int lobbyPin, String username, boolean createdSession) {
        this.lobbyPin = lobbyPin;
        this.username = username;
        this.createdSession = createdSession;
    }

    public int getLobbyPin() {
        return lobbyPin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isCreatedSession() {
        return createdSession;
    }
}
