package use_cases.AddQuestion;

import api_caller.api_caller;
import entities.Lobby;
import entities.Question;
import entities.QuestionFactory;

import java.net.MalformedURLException;
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
/*        System.out.println("Interactor: execute called with prompt: " + prompt + "lobbypin: " + Integer.toString(lobbypin));*/



        Question question = questionFactory.createQuestion(prompt, choices, correctIndex);
        Lobby lobby = lobbyDataAccess.getLobby(); // Here get lobby by lobbyPin

        if (lobby == null) {
            presenter.prepareFailView("Lobby not found");
            return;
            //Change this if add multiple lobbies
        }else {

            lobby.addQuestion(question);
            lobbyDataAccess.saveLobby(lobby);
//            System.out.println("Question added: " + question.getPrompt());
//            System.out.println("Question added: " + lobby.getQuestions().toString());

            AddQuestionOutputData outputData = new AddQuestionOutputData("Question added successfully!");
            presenter.prepareSuccessView(outputData);
        }
        //
        //Create Question from input data using Question Factory
        // Need to get Lobby object stored in MemoryDataAccessObject
        //Now I need to make some output data
    }

    @Override
    public void sendQuestions(int lobbyPin) throws MalformedURLException {
        api_caller apiCaller = new api_caller();
        Lobby lobby = lobbyDataAccess.getLobby();
        //Need to update Get lobby with pin
        Question[] Questions = lobby.getQuestions().toArray(new Question[0]);
        apiCaller.sendQuestions(String.valueOf(lobbyPin), Questions);

    }
}
