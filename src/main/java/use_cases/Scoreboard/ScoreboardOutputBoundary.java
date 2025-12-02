package use_cases.Scoreboard;

/**
 * Output boundary for scoreboard use case.
 * Defines methods for presenting success or failure results to the user interface.
 */
public interface ScoreboardOutputBoundary {
    /**
     * Present the scoreboard when results are loaded or saved successfully.
     *
     * @param data the output data containing the scoreboard rows to display
     */
    void prepareSuccessView(ScoreboardOutputData data);
    /**
     * Present an error message when the scoreboard action fails.
     *
     * @param errorMessage the error message explaining the failure
     */

    void prepareFailView(String errorMessage);
}
