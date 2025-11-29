package interface_adapters.AddQuestion;

import use_cases.AddQuestion.AddQuestionOutputBoundary;
import use_cases.AddQuestion.AddQuestionOutputData;



public class AddQuestionPresenter implements AddQuestionOutputBoundary {
    private final LobbyPrepViewModel lobbyPrepViewModel;

    public AddQuestionPresenter(LobbyPrepViewModel lobbyPrepViewModel) {
        this.lobbyPrepViewModel = lobbyPrepViewModel;
    }

    @Override
    public void prepareSuccessView(AddQuestionOutputData outputData) {
        // Increment number of questions
//        System.out.println("Presenter: prepareSuccessView called with message: " + outputData.getMessage());
        lobbyPrepViewModel.setPopupMessage(outputData.getMessage());
        lobbyPrepViewModel.incrementQuestionCount();


    }

    @Override
    public void prepareFailView(String errorMessage) {
//        System.out.println("Presenter: prepareFailview called with message: " + errorMessage);
        lobbyPrepViewModel.setPopupMessage(errorMessage);


    }
}
//prepare success view and prepa;re fail view
// Tell view manager to show them next view?