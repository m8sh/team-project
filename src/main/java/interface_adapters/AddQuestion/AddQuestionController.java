package interface_adapters.AddQuestion;

import use_cases.addQuestion.AddQuestionInputBoundary;
import use_cases.addQuestion.AddQuestionInputData;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class AddQuestionController {
    private final AddQuestionInputBoundary addQuestionInteractor;

    public AddQuestionController(AddQuestionInputBoundary addQuestionInteractor) {
        this.addQuestionInteractor = addQuestionInteractor;
    }

    public void execute(String prompt, ArrayList<String> choices, int correctIndex, int lobbyPin) {
        final AddQuestionInputData InputData = new AddQuestionInputData(prompt, choices, correctIndex, lobbyPin);
        addQuestionInteractor.execute(InputData);
        //how to mark correct index?
    }
    public void sendQuestions(int lobbyPin) throws MalformedURLException {
        addQuestionInteractor.sendQuestions(lobbyPin);

    };
}
