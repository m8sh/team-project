package use_cases.StartScreen;

public class JoinSessionInputData {

    private final String lobbyPin;
    private final String username;

    public JoinSessionInputData(String lobbyPin, String username) {
        this.lobbyPin = lobbyPin;
        this.username = username;
    }

    public String getLobbyPin() {
        return lobbyPin;
    }

    public String getUsername() {
        return username;
    }

}
