package interface_adapters.scoreboard;

import use_cases.Scoreboard.ScoreboardInputBoundary;
import use_cases.Scoreboard.ScoreboardInputData;

public class ScoreboardController {

    private final ScoreboardInputBoundary interactor;

    public ScoreboardController(ScoreboardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void showScoreboard(int pin) {
        ScoreboardInputData scoreboardInputData = new ScoreboardInputData(pin);
        interactor.showScoreboard(scoreboardInputData);
    }

    public void endSession() {
        // Go back to Lobby
    }

    public void execute(int pin) {
        ScoreboardInputData scoreboardInputData = new ScoreboardInputData(pin);
        interactor.showScoreboard(scoreboardInputData);
    }
}

