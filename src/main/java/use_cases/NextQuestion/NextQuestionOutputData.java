package use_cases.NextQuestion;

import entities.Question;

public class NextQuestionOutputData {
    private final Question question;
    private final String message;

    public NextQuestionOutputData(Question question, String message) {
        this.question = question;
        this.message = message;
    }

    public Question getQuestion() {
        return question;
    }

    public String getMessage() {
        return message;
    }
}