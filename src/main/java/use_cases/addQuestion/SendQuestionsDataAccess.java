package use_cases.addQuestion;

import entities.Question;

import java.net.MalformedURLException;

public interface SendQuestionsDataAccess {
    void sendQuestions(String pin, Question[] questionList) throws MalformedURLException;
}
