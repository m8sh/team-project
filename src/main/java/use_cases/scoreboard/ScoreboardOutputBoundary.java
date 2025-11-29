package use_cases.scoreboard;

public interface ScoreboardOutputBoundary {

    void prepareSuccessView(ScoreboardOutputData scoreboardOutputData);
    void prepareFailView(String errorMessage);
}
