package use_cases.StartScreen;

public interface    StartScreenOutputBoundary {

    void prepareJoinSuccessView(StartScreenOutputData startScreenOutputData);
    void prepareJoinFailureView(String errorMessage);

    void prepareCreateSessionSuccessView(StartScreenOutputData startScreenOutputData);
    void prepareCreateSessionFailureView(String errorMessage);

}
