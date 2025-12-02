package interface_adapters.Scoreboard;

import entities.User;
import use_cases.Scoreboard.ScoreboardInputBoundary;
import use_cases.Scoreboard.ScoreboardInputData;
import use_cases.Scoreboard.ScoreboardSaveInputData;


import java.util.List;

public class ScoreboardController {

    private final ScoreboardInputBoundary interactor;

    public ScoreboardController(ScoreboardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showScoreboard(int lobbyPin) {
        ScoreboardInputData inputData = new ScoreboardInputData(lobbyPin);
        interactor.showScoreboard(inputData);
    }

    public void saveResults(int lobbyPin, List<User> users) {
        ScoreboardSaveInputData inputData = new ScoreboardSaveInputData(lobbyPin, users);
        interactor.saveResults(inputData);
    }


}
