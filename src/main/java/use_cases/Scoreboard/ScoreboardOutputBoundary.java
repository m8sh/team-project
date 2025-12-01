package use_cases.Scoreboard;

public interface ScoreboardOutputBoundary {

    void prepareSuccessView(ScoreboardOutputData scoreboardOutputData);
    void prepareFailView(String errorMessage);
}
