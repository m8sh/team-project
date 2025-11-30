package interface_adapters.StartScreen;

import use_cases.StartScreen.CreateSessionInputData;
import use_cases.StartScreen.StartScreenInputBoundary;
import use_cases.StartScreen.JoinSessionInputData;

public class StartScreenController {

    private final StartScreenInputBoundary interactor;

    public StartScreenController(StartScreenInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onJoinRequested(String pin, String username) {
        JoinSessionInputData inputData = new JoinSessionInputData(pin, username);
        interactor.joinSession(inputData);
    }

    public void onCreateSessionRequested() {
        CreateSessionInputData inputData = new CreateSessionInputData();
        interactor.createSession(inputData);
    }

}
