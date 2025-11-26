package use_cases.AddQuestion;

import entities.Lobby;
import entities.Question;
import entities.QuestionFactory;

import java.util.List;

public class AddQuestionInteractor implements AddQuestionInputBoundary {
    private final AddQuestionLobbyDataAccessInterface lobbyDataAccess;
    private final AddQuestionOutputBoundary presenter;
    private final QuestionFactory questionFactory;

    public AddQuestionInteractor(AddQuestionLobbyDataAccessInterface lobbyDataAccess,
                                 AddQuestionOutputBoundary presenter,
                                 QuestionFactory questionFactory){
        this.lobbyDataAccess = lobbyDataAccess;
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



        Question question = questionFactory.createQuestion(prompt, choices, correctIndex);
        Lobby lobby = lobbyDataAccess.getLobby(); // Here get lobby by lobbyPin

        if (lobby == null) {
            presenter.prepareFailView("Lobby not found");
            return;
        }else {

            lobby.addQuestion(question);
            lobbyDataAccess.saveLobby(lobby);
            AddQuestionOutputData outputData = new AddQuestionOutputData("Question added successfully!");
            presenter.prepareSuccessView(outputData);
        }
    }
}
