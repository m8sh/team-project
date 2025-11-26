package use_cases.NextQuestion;

import entities.Question;
import entities.User;

import java.util.List;

public class NextQuestionInputData {
    private final String lobbyPin;           // Keep the pin
    private final List<Question> questions;
    private final User currentPlayer;

    public NextQuestionInputData(String lobbyPin, List<Question> questions, User currentPlayer) {
        this.lobbyPin = lobbyPin;
        this.questions = questions;
        this.currentPlayer = currentPlayer;
    }

    public String getLobbyPin() { return lobbyPin; }
    public List<Question> getQuestions() { return questions; }
    public User getCurrentPlayer() { return currentPlayer; }
}