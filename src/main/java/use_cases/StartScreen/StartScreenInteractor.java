package use_cases.StartScreen;

import entities.Lobby;

public class StartScreenInteractor implements StartScreenInputBoundary {
    private final StartScreenLobbyDataAccessInterface startScreenLobbyDataAccess;
    private final StartScreenNetworkDataAccessInterface startScreenNetworkDataAccess;
    private final StartScreenOutputBoundary startScreenOutputBoundary;

    public StartScreenInteractor(StartScreenLobbyDataAccessInterface startScreenLobbyDataAccess,
                                 StartScreenNetworkDataAccessInterface startScreenNetworkDataAccess,
                                 StartScreenOutputBoundary startScreenOutputBoundary) {
        this.startScreenLobbyDataAccess = startScreenLobbyDataAccess;
        this.startScreenNetworkDataAccess = startScreenNetworkDataAccess;
        this.startScreenOutputBoundary = startScreenOutputBoundary;
    }

    public void joinSession(JoinSessionInputData joinSessionInputData) {
        String pinText = joinSessionInputData.getLobbyPin();
        String username = joinSessionInputData.getUsername();

        if (pinText == null || pinText.isEmpty()) {
            startScreenOutputBoundary.prepareJoinFailureView("PIN cannot be empty.");
            return;
        }
        if (username == null || username.isEmpty()) {
            startScreenOutputBoundary.prepareJoinFailureView("Username cannot be empty.");
            return;
        }

        final int pin;
        try {
            pin = Integer.parseInt(pinText);
        }
        catch (NumberFormatException e) {
            startScreenOutputBoundary.prepareJoinFailureView("Pin must be an integer.");
            return;
        }

        Lobby lobby = startScreenLobbyDataAccess.getLobby(pin);
        if (lobby == null) {
            startScreenOutputBoundary.prepareJoinFailureView("Lobby with that PIN does not exist.");
            return;
        }

        boolean joinedLobby = startScreenNetworkDataAccess.joinRemoteRoom(pin, username);
        if (!joinedLobby) {
            startScreenOutputBoundary.prepareJoinFailureView("Couldn't connect to server.");
            return;
        }

        StartScreenOutputData outputData = new StartScreenOutputData(pin, username, false);
        startScreenOutputBoundary.prepareJoinSuccessView(outputData);
    }

    public void createSession(CreateSessionInputData inputData){
        int pin = 100000 + (int) (Math.random() * 899999);

        while (startScreenLobbyDataAccess.getLobby(pin) != null){
            pin = 100000 + (int) (Math.random() * 899999);

        }

        Lobby lobby = new Lobby(pin);
        startScreenLobbyDataAccess.saveLobby(lobby);

        boolean createdLobby = startScreenNetworkDataAccess.createRemoteRoom(pin);
        if (!createdLobby) {
            startScreenOutputBoundary.prepareCreateSessionFailureView("Couldn't create a room.");
            return;
        }

        StartScreenOutputData outputData = new StartScreenOutputData(pin, null, true);
        startScreenOutputBoundary.prepareCreateSessionSuccessView(outputData);
    }

}
