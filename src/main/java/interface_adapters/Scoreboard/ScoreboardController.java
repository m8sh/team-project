package interface_adapters.Scoreboard;

import java.util.List;

import entities.User;
import use_cases.Scoreboard.ScoreboardInputBoundary;
import use_cases.Scoreboard.ScoreboardInputData;
import use_cases.Scoreboard.ScoreboardSaveInputData;
/**
 * Controller responsible for handling actions related to the scoreboard.
 * It creates and sends input data to the corresponding use case interactor
 * to either display the scoreboard or save the final results.
 */

public class ScoreboardController {

    private final ScoreboardInputBoundary interactor;
    /**
     * Create a new controller for scoreboard operations.
     *
     * @param interactor the use case interactor that processes scoreboard actions
     */

    public ScoreboardController(ScoreboardInputBoundary interactor) {
        this.interactor = interactor;
    }
    /**
     * Request the scoreboard to be displayed for the specified lobby.
     *
     * @param lobbyPin the identifier of the lobby whose scoreboard should be shown
     */

    public void showScoreboard(int lobbyPin) {
        final ScoreboardInputData inputData = new ScoreboardInputData(lobbyPin);
        interactor.showScoreboard(inputData);
    }
    /**
     * Save the final scoreboard results for the specified lobby.
     *
     * @param lobbyPin the identifier of the lobby
     * @param users the list of users and their updated scores
     */

    public void saveResults(int lobbyPin, List<User> users) {
        final ScoreboardSaveInputData inputData = new ScoreboardSaveInputData(lobbyPin, users);
        interactor.saveResults(inputData);
    }
}
