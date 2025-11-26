package interfaceAdapters.NextQuestion;

import use_cases.NextQuestion.NextQuestionInteractor;
import use_cases.NextQuestion.NextQuestionInputData;
import entities.User;
import entities.Question;

import java.util.List;

public class NextQuestionController {

    private final NextQuestionInteractor interactor;

    public NextQuestionController(NextQuestionInteractor interactor) {
        this.interactor = interactor;
    }

    public void execute(String lobbyPin, List<Question> questions, User currentPlayer) {
        NextQuestionInputData inputData = new NextQuestionInputData(lobbyPin, questions, currentPlayer);
        interactor.execute(inputData);
    }
}
