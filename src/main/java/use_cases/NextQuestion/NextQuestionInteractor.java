package use_cases.NextQuestion;

import entities.Lobby;
import entities.Question;
import entities.User;

import java.util.List;

public class NextQuestionInteractor implements NextQuestionInputBoundary{

    private final NextQuestionLobbyDataAccessInterface lobbyDataAccess;
    private final NextQuestionOutputBoundary presenter;

    public NextQuestionInteractor(NextQuestionLobbyDataAccessInterface lobbyDataAccess,
                                  NextQuestionOutputBoundary presenter) {
        this.lobbyDataAccess = lobbyDataAccess;
        this.presenter = presenter;
    }

    public void execute(NextQuestionInputData inputData) {
        String lobbyPin = inputData.getLobbyPin();
        User currentPlayer = inputData.getCurrentPlayer();

        Lobby lobby = lobbyDataAccess.getLobby();
        if (lobby == null) {
            presenter.prepareFailView("Lobby not found for pin: " + lobbyPin);
            return;
        }

        List<Question> questions = inputData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            presenter.prepareFailView("No questions provided");
            return;
        }

        Question nextQuestion = questions.get(0);

        NextQuestionOutputData outputData = new NextQuestionOutputData(nextQuestion, "Next question ready");
    }

}
