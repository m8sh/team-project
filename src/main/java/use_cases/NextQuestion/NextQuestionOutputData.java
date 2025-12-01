package use_cases.NextQuestion;

import entities.Question;
import use_cases.scoreboard.ScoreboardOutputData;

import java.util.List;

public class NextQuestionOutputData {
    private final Question question;
    private final String message; // optional, e.g., "No more questions"
    private final List<ScoreboardOutputData.Row> scoreboardRows;

    public NextQuestionOutputData(Question question, String message,
                                  List<ScoreboardOutputData.Row> scoreboardRows) {
        this.question = question;
        this.message = message;
        this.scoreboardRows = scoreboardRows;
    }

    public Question getQuestion() {
        return question;
    }

    public String getMessage() {
        return message;
    }

    public List<ScoreboardOutputData.Row> getScoreboardRows() {
        return scoreboardRows;
    }
}