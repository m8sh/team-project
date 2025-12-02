package use_cases.Scoreboard;

public interface ScoreboardOutputBoundary {
    void prepareSuccessView(ScoreboardOutputData data);
    void prepareFailView(String errorMessage);
    void endSession();
}
