package use_cases.Scoreboard;

public interface ScoreboardInputBoundary {

    void showScoreboard(ScoreboardInputData inputData);


    void saveResults(ScoreboardSaveInputData inputData);

    void endSession();
}
