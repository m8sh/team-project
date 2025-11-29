package use_cases.AddQuestion;

import entities.Question;

import java.net.MalformedURLException;

public interface SendQuestionsDataAccess {
    void sendQuestions(String pin, Question[] questionList) throws MalformedURLException;
}
