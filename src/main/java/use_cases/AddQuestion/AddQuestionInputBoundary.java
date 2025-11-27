package use_cases.AddQuestion;

import java.net.MalformedURLException;

public interface AddQuestionInputBoundary {
    void execute(AddQuestionInputData inputData);
    void sendQuestions(int lobbyPin) throws MalformedURLException;
}
