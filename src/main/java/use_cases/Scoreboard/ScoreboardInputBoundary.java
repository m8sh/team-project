package use_cases.Scoreboard;

/**
 * Input boundary for the scoreboard use case.
 * Implementations handle showing the scoreboard and saving results.
 */

public interface ScoreboardInputBoundary {
    /**
     * Show the scoreboard for a lobby.
     *
     * @param inputData the data needed to display the scoreboard
     */
    void showScoreboard(ScoreboardInputData inputData);
    /**
     * Save the users and their scores for a lobby.
     *
     * @param inputData the data containing the scores to save
     */

    void saveResults(ScoreboardSaveInputData inputData);

}
