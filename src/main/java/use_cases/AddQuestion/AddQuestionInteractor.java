package use_cases.AddQuestion;

import entities.Lobby;
import entities.Question;
import entities.QuestionFactory;

import java.util.List;

public class AddQuestionInteractor implements AddQuestionInputBoundary {
    private final AddQuestionLobbyDataAccessInterface LobbyDataAccess;
    private final AddQuestionOutputBoundary presenter;
    private final QuestionFactory questionFactory;

    public AddQuestionInteractor(AddQuestionLobbyDataAccessInterface LobbyDataAccess,
                                 AddQuestionOutputBoundary presenter,
                                 QuestionFactory questionFactory){
        this.LobbyDataAccess = LobbyDataAccess;
        this.presenter = presenter;
        this.questionFactory = questionFactory;
    }

    // should make a question based on input data, make question entity that we want to add to the lobby
    @Override
    public void execute(AddQuestionInputData inputData) {
        String prompt = inputData.getPrompt();
        int lobbypin = inputData.getLobbyPin();
        List<String> choices = inputData.getChoices();
        int correctIndex = inputData.getCorrectIndex();

        if(choices.size() < 2){
            // Make the presenter notify the user that a question must have at least two answer choices
        }
        if (correctIndex < 0 || correctIndex >= choices.size()) {
            //Correct answer index is invalid
            //What to do in this scenario?
        }

        Question question = questionFactory.createQuestion(prompt, choices, correctIndex);
        Lobby lobby = LobbyDataAccess.getLobby();
        lobby.addQuestion(question);
        //Create Question from input data using Question Factory
        // Need to get Lobby object stored in MemoryDataAccessObject


    }
}
