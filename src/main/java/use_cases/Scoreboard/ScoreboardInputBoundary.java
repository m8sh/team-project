package use_cases.Scoreboard;

public interface ScoreboardInputBoundary {

    void showScoreboard(ScoreboardShowInputData inputData);

    void saveResults(ScoreboardSaveInputData inputData);

    void endSession();
}
