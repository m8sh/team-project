package use_cases.addQuestion;

import java.util.List;

public class AddQuestionInputData {
    private final String prompt;
    private final int lobbyPin;
    private final List<String> choices;
    private final int correctIndex;
    public AddQuestionInputData(String prompt, List<String> choices, int correctIndex, int lobbyPin) {
        this.prompt = prompt;
        this.lobbyPin = lobbyPin;
        this.choices = choices;
        this.correctIndex = correctIndex;

    }
    String getPrompt() {return prompt;}
    int getLobbyPin() {return lobbyPin;}
    List<String> getChoices() {return choices;}
    int getCorrectIndex() {return correctIndex;}
}
