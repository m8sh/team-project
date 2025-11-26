package use_cases.NextQuestion;

import entities.Question;

public interface NextQuestionOutputBoundary {

    void prepareNextQuestion(Question question);

    void prepareFailView(String errorMessage);
}