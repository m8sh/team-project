package interface_adapters.scoreboard;

import entities.User;
import use_cases.Scoreboard.ScoreboardInputBoundary;
import use_cases.Scoreboard.ScoreboardSaveInputData;
import use_cases.Scoreboard.ScoreboardShowInputData;

import java.util.List;

public class ScoreboardController {

    private final ScoreboardInputBoundary interactor;

    public ScoreboardController(ScoreboardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showScoreboard(int lobbyPin) {
        ScoreboardShowInputData inputData = new ScoreboardShowInputData(lobbyPin);
        interactor.showScoreboard(inputData);
    }

    public void saveResults(int lobbyPin, List<User> users) {
        ScoreboardSaveInputData inputData = new ScoreboardSaveInputData(lobbyPin, users);
        interactor.saveResults(inputData);
    }

    public void endSession() {
        interactor.endSession();
    }
}
